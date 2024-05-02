package pl.bodzioch.damian.service_provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.client.bur.BurServiceProviderDto;
import pl.bodzioch.damian.client.bur.IBurClient;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.service_provider.command_dto.CreateNewServiceProviderCommand;
import pl.bodzioch.damian.service_provider.command_dto.CreateNewServiceProviderCommandResult;
import pl.bodzioch.damian.utils.MessageResolver;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
class CreateNewServiceProviderCommandHandler implements CommandHandler<CreateNewServiceProviderCommand, CreateNewServiceProviderCommandResult> {

    private final IProviderWriteRepository writeRepository;
    private final IBurClient burClient;
    private final MessageResolver messageResolver;

    @Override
    public Class<CreateNewServiceProviderCommand> commandClass() {
        return CreateNewServiceProviderCommand.class;
    }

    @Override
    public CreateNewServiceProviderCommandResult handle(CreateNewServiceProviderCommand command) {
        Optional<Long> burId = burClient.getServiceProvider(command.nip())
                .onErrorComplete()
                .blockOptional()
                .map(BurServiceProviderDto::id);
        ServiceProvider serviceProvider = new ServiceProvider(command, burId.orElse(null));
        try {
            writeRepository.createNew(serviceProvider);
        } catch (DuplicateKeyException e) {
            log.warn("Service provider with NIP: {} already exists", command.nip(), e);
            throw buildProviderByNipAlreadyExistsException(command.nip());
        }
        String message = messageResolver.getMessage("serviceProvider.createNewServiceProviderSuccess");
        return new CreateNewServiceProviderCommandResult(message);
    }

    private AppException buildProviderByNipAlreadyExistsException(Long nip) {
        return new AppException(
                HttpStatus.BAD_REQUEST,
                List.of(new ErrorData(
                        "error.client.serviceProvider.nipAlreadyExists",
                        List.of(nip.toString())
                ))
        );
    }
}
