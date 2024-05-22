package pl.bodzioch.damian.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.client.bur.BurServiceDto;
import pl.bodzioch.damian.client.bur.IBurClient;
import pl.bodzioch.damian.exception.HttpClientException;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.service.command_dto.CreateNewServiceCommand;
import pl.bodzioch.damian.service.command_dto.CreateNewServiceCommandResult;
import pl.bodzioch.damian.utils.MessageResolver;
import pl.bodzioch.damian.value_object.ErrorData;

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

	@Override
	public Class<CreateNewServiceCommand> commandClass() {
		return CreateNewServiceCommand.class;
	}

	@Override
	public CreateNewServiceCommandResult handle(CreateNewServiceCommand command) {
		BurServiceNumber burServiceNumber = new BurServiceNumber(command.number());
		Long burServiceId = burServiceNumber.getBurServiceId();
		Optional<BurServiceDto> burService = getBurService(burServiceId);
		burService.ifPresent(service -> validateCommand(service, command));
		return null;
	}

	private void validateCommand(BurServiceDto burService, CreateNewServiceCommand command) {
		List<ErrorData> errors = new ArrayList<>();
		if (!burService.serviceTypeId().equals((long) command.type().getBurId())) {
			errors.add(buildIncorrectServiceTypeError(burService.serviceTypeId(), command.type().getBurId()));
		}
		//TODO walidacja service providera
	}

	private Optional<BurServiceDto> getBurService(Long burServiceId) {
		try {
			return Optional.ofNullable(burClient.getService(burServiceId));
		} catch(HttpClientException e) {
			log.warn("Bur client exception", e);
		}
		return Optional.empty();
	}

	private ErrorData buildIncorrectServiceTypeError(Long burType, Integer formType) {
		String burServiceType = messageResolver.getMessage("service.type." + ServiceType.ofBurId(burType.intValue()));
		String formServiceType = messageResolver.getMessage("service.type." + ServiceType.ofBurId(formType));
		log.warn("Incorrect service type. Bur id: " + burType + ", form id: " + formType);
		return new ErrorData("error.client.service.burIncorrectServiceType", List.of(burServiceType, formServiceType));
	}


}
