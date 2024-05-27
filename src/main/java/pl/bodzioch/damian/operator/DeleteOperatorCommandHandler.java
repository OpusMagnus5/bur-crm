package pl.bodzioch.damian.operator;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.operator.command_dto.DeleteOperatorCommand;
import pl.bodzioch.damian.operator.command_dto.DeleteOperatorCommandResult;
import pl.bodzioch.damian.utils.MessageResolver;

@CacheEvict(value = "operators", allEntries = true)
@Component
@RequiredArgsConstructor
class DeleteOperatorCommandHandler implements CommandHandler<DeleteOperatorCommand, DeleteOperatorCommandResult> {

    private final IOperatorWriteRepository writeRepository;
    private final MessageResolver messageResolver;

    @Override
    public Class<DeleteOperatorCommand> commandClass() {
        return DeleteOperatorCommand.class;
    }

    @Override
    public DeleteOperatorCommandResult handle(DeleteOperatorCommand command) {
        writeRepository.delete(command.id());
        String message = messageResolver.getMessage("operator.deleteByIdSuccess");
        return new DeleteOperatorCommandResult(message);
    }
}
