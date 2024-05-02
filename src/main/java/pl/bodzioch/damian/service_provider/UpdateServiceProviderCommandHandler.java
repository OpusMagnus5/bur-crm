package pl.bodzioch.damian.service_provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.service_provider.command_dto.UpdateServiceProviderCommand;
import pl.bodzioch.damian.service_provider.command_dto.UpdateServiceProviderCommandResult;
import pl.bodzioch.damian.utils.MessageResolver;

@Component
@RequiredArgsConstructor
class UpdateServiceProviderCommandHandler implements CommandHandler<UpdateServiceProviderCommand, UpdateServiceProviderCommandResult> {

    private final IProviderWriteRepository writeRepository;
    private final MessageResolver messageResolver;

    @Override
    public Class<UpdateServiceProviderCommand> commandClass() {
        return UpdateServiceProviderCommand.class;
    }

    @Override
    public UpdateServiceProviderCommandResult handle(UpdateServiceProviderCommand command) {
        writeRepository.update(new ServiceProvider(command));
        String message = messageResolver.getMessage("serviceProvider.updateSuccess");
        return new UpdateServiceProviderCommandResult(message);
    }
}
