package pl.bodzioch.damian.service_provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.service_provider.command_dto.DeleteServiceProviderCommand;
import pl.bodzioch.damian.service_provider.command_dto.DeleteServiceProviderCommandResult;
import pl.bodzioch.damian.utils.MessageResolver;

@Component
@RequiredArgsConstructor
class DeleteServiceProviderCommandHandler implements CommandHandler<DeleteServiceProviderCommand, DeleteServiceProviderCommandResult> {

    private final MessageResolver messageResolver;
    private final IProviderWriteRepository writeRepository;

    @Override
    public Class<DeleteServiceProviderCommand> commandClass() {
        return DeleteServiceProviderCommand.class;
    }

    @Override
    public DeleteServiceProviderCommandResult handle(DeleteServiceProviderCommand command) {
        writeRepository.delete(command.id());
        String message = messageResolver.getMessage("serviceProvider.deleteByIdSuccess");
        return new DeleteServiceProviderCommandResult(message);
    }
}
