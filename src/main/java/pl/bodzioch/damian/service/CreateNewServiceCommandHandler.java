package pl.bodzioch.damian.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.client.bur.BurServiceDto;
import pl.bodzioch.damian.client.bur.IBurClient;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.exception.HttpClientException;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.service.command_dto.CreateNewServiceCommand;
import pl.bodzioch.damian.service.command_dto.CreateNewServiceCommandResult;
import pl.bodzioch.damian.service_provider.BurServiceProvider;
import pl.bodzioch.damian.service_provider.query_dto.GetServiceProviderDetailsQuery;
import pl.bodzioch.damian.service_provider.query_dto.GetServiceProviderDetailsQueryResult;
import pl.bodzioch.damian.utils.MessageResolver;
import pl.bodzioch.damian.value_object.ErrorData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
class CreateNewServiceCommandHandler implements CommandHandler<CreateNewServiceCommand, CreateNewServiceCommandResult> {

	private final IServiceWriteRepository writeRepository;
	private final IBurClient burClient;
	private final MessageResolver messageResolver;
	private final QueryExecutor queryExecutor;

	@Override
	public Class<CreateNewServiceCommand> commandClass() {
		return CreateNewServiceCommand.class;
	}

	@Override
	public CreateNewServiceCommandResult handle(CreateNewServiceCommand command) {
		List<String> messages = new ArrayList<>();
		BurServiceNumber burServiceNumber = new BurServiceNumber(command.number());
		Long burServiceId = burServiceNumber.getBurServiceId();
		Optional<BurServiceDto> burService = getBurService(burServiceId);
		if (burService.isPresent()) {
			validateCommand(burService.get(), command);
		} else {
			log.warn("Service not found in bur client!");
			messages.add(messageResolver.getMessage("service.serviceNotFoundInBur"));
		}
		Long cardId = burService.map(BurServiceDto::id).orElse(null);
		Service service = new Service(command, cardId);
		writeRepository.createNew(service);
		messages.add(messageResolver.getMessage("service.createNewServiceSuccess"));
		return new CreateNewServiceCommandResult(messages);
	}

	private Optional<BurServiceDto> getBurService(Long burServiceId) {
		try {
			return Optional.ofNullable(burClient.getService(burServiceId));
		} catch(HttpClientException e) {
			log.warn("Bur client exception", e);
		}
		return Optional.empty();
	}

	private void validateCommand(BurServiceDto burService, CreateNewServiceCommand command) {
		List<ErrorData> errors = new ArrayList<>(); //
		validateServiceType(burService, command).ifPresent(errors::add);
		validateServiceProvider(burService, command).ifPresent(errors::add); //TODO Validate czy startDate jest wcześniej niż endDate
		validateStartDate(burService, command).ifPresent(errors::add);
		validateEndDate(burService, command).ifPresent(errors::add);
		validateNumber(burService, command).ifPresent(errors::add);

		if (!errors.isEmpty()) {
			throw new AppException("Registered errors during validation", HttpStatus.BAD_REQUEST, errors);
		}
	}

	private Optional<ErrorData> validateServiceType(BurServiceDto burService, CreateNewServiceCommand command) {
		Long burServiceTypeId = burService.serviceTypeId();
		long formServiceTypeId = command.type().getBurId();
		if (burServiceTypeId != formServiceTypeId) {
			return Optional.of(buildIncorrectServiceTypeError(burServiceTypeId, formServiceTypeId));
		}
		return Optional.empty();
	}

	private Optional<ErrorData> validateServiceProvider(BurServiceDto burService, CreateNewServiceCommand command) {
		GetServiceProviderDetailsQuery query = new GetServiceProviderDetailsQuery(command.serviceProviderId());
		GetServiceProviderDetailsQueryResult result = queryExecutor.execute(query);
		Long serviceProviderBurId = result.serviceProvider().burId();
		Long serviceProviderFormId = burService.serviceProvider().id();
		if (serviceProviderFormId.compareTo(serviceProviderBurId) != 0) {
			return Optional.of(buildIncorrectServiceProviderError(serviceProviderBurId, serviceProviderFormId));
		}
		return Optional.empty();
	}

	private Optional<ErrorData> validateStartDate(BurServiceDto burService, CreateNewServiceCommand command) {
		LocalDate burStartDate = burService.startDate().toLocalDate();
		LocalDate formStartDate = command.startDate();
		if (!burStartDate.isEqual(formStartDate)) {
			return Optional.of(buildIncorrectStartDateError(burStartDate, formStartDate));
		}
		return Optional.empty();
	}

	private Optional<ErrorData> validateEndDate(BurServiceDto burService, CreateNewServiceCommand command) {
		LocalDate burEndDate = burService.startDate().toLocalDate();
		LocalDate formEndDate = command.startDate();
		if (!burEndDate.isEqual(formEndDate)) {
			return Optional.of(buildIncorrectEndDateError(burEndDate, formEndDate));
		}
		return Optional.empty();
	}

	private Optional<ErrorData> validateNumber(BurServiceDto burService, CreateNewServiceCommand command) {
		String burNumber = burService.number();
		String formNumber = command.number();
		if (!burNumber.trim().equals(formNumber)) {
			return Optional.of(buildIncorrectNumberError(burNumber, formNumber));
		}
		return Optional.empty();
	}

	private ErrorData buildIncorrectServiceTypeError(Long burType, Long formType) {
		String burServiceType = messageResolver.getMessage("service.type." + ServiceType.ofBurId(burType));
		String formServiceType = messageResolver.getMessage("service.type." + ServiceType.ofBurId(formType));
		log.warn("Incorrect service type. Bur id: " + burType + ", form id: " + formType);
		return new ErrorData("error.client.service.burIncorrectServiceType", List.of(formServiceType, burServiceType));
	}

	private ErrorData buildIncorrectServiceProviderError(Long burProviderId, Long formProviderId) {
		String burProvider = messageResolver.getMessage("serviceProvider." + BurServiceProvider.ofBurId(burProviderId));
		String formProvider = messageResolver.getMessage("serviceProvider." + BurServiceProvider.ofBurId(formProviderId));
		log.warn("Incorrect service provider. Bur id: " + burProviderId + ", form id: " + formProviderId);
		return new ErrorData("error.client.service.burIncorrectServiceProvider", List.of(formProvider, burProvider));
	}

	private ErrorData buildIncorrectStartDateError(LocalDate burDate, LocalDate formDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String formattedBurDate = formatter.format(burDate);
		String formattedFormDate = formatter.format(formDate);
		log.warn("Incorrect service start date. Bur date: " + burDate + ", form date: " + formDate);
		return new ErrorData("error.client.service.burIncorrectStartDate", List.of(formattedFormDate, formattedBurDate));
	}

	private ErrorData buildIncorrectEndDateError(LocalDate burDate, LocalDate formDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String formattedBurDate = formatter.format(burDate);
		String formattedFormDate = formatter.format(formDate);
		log.warn("Incorrect service end date. Bur date: " + burDate + ", form date: " + formDate);
		return new ErrorData("error.client.service.burIncorrectEndDate", List.of(formattedFormDate, formattedBurDate));
	}

	private ErrorData buildIncorrectNumberError(String burNumber, String formNumber) {
		log.warn("Incorrect service number. Bur number: " + burNumber + ", form number: " + formNumber);
		return new ErrorData("error.client.service.burIncorrectNumber", List.of(formNumber, burNumber));
	}
}
