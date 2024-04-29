package pl.bodzioch.damian.service_provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.service_provider.command_dto.CreateNewServiceProviderCommand;
import pl.bodzioch.damian.service_provider.command_dto.CreateNewServiceProviderCommandResult;

@Component
@RequiredArgsConstructor
class CreateNewServiceProviderCommandHandler implements CommandHandler<CreateNewServiceProviderCommand, CreateNewServiceProviderCommandResult> {

    private final IProviderWriteRepository writeRepository;

    @Override
    public Class<CreateNewServiceProviderCommand> commandClass() {
        return CreateNewServiceProviderCommand.class;
    }

    @Override
    public CreateNewServiceProviderCommandResult handle(CreateNewServiceProviderCommand command) {
        return null;
    }
}
