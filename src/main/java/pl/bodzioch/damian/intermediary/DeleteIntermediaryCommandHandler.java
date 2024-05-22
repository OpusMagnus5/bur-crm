package pl.bodzioch.damian.intermediary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.intermediary.command_dto.DeleteIntermediaryCommand;
import pl.bodzioch.damian.intermediary.command_dto.DeleteIntermediaryCommandResult;
import pl.bodzioch.damian.utils.MessageResolver;

@Component
@RequiredArgsConstructor
class DeleteIntermediaryCommandHandler implements CommandHandler<DeleteIntermediaryCommand, DeleteIntermediaryCommandResult> {

    private final IIntermediaryWriteRepository writeRepository;
    private final MessageResolver messageResolver;

    @Override
    public Class<DeleteIntermediaryCommand> commandClass() {
        return DeleteIntermediaryCommand.class;
    }

    @Override
    public DeleteIntermediaryCommandResult handle(DeleteIntermediaryCommand command) {
        writeRepository.delete(command.id());
        String message = messageResolver.getMessage("intermediary.deleteByIdSuccess");
        return new DeleteIntermediaryCommandResult(message);
    }
}
