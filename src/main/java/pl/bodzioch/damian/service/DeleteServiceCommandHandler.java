package pl.bodzioch.damian.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.service.command_dto.DeleteServiceCommand;
import pl.bodzioch.damian.service.command_dto.DeleteServiceCommandResult;
import pl.bodzioch.damian.utils.MessageResolver;

@Component
@RequiredArgsConstructor
class DeleteServiceCommandHandler implements CommandHandler<DeleteServiceCommand, DeleteServiceCommandResult> {

    private final IServiceWriteRepository writeRepository;
    private final MessageResolver messageResolver;

    @Override
    public Class<DeleteServiceCommand> commandClass() {
        return DeleteServiceCommand.class;
    }

    @Override
    public DeleteServiceCommandResult handle(DeleteServiceCommand command) {
        writeRepository.delete(command.id());
        String message = messageResolver.getMessage("service.deleteByIdSuccess");
        return new DeleteServiceCommandResult(message);
    }
}
