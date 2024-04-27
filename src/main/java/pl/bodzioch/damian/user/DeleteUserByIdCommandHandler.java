package pl.bodzioch.damian.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.user.commandDto.DeleteUserByIdCommand;
import pl.bodzioch.damian.user.commandDto.DeleteUserByIdCommandResult;
import pl.bodzioch.damian.utils.MessageResolver;

@Component
@RequiredArgsConstructor
class DeleteUserByIdCommandHandler implements CommandHandler<DeleteUserByIdCommand, DeleteUserByIdCommandResult> {

    private final MessageResolver messageResolver;
    private final IUserWriteRepository writeRepository;

    @Override
    public Class<DeleteUserByIdCommand> commandClass() {
        return DeleteUserByIdCommand.class;
    }

    @Override
    public DeleteUserByIdCommandResult handle(DeleteUserByIdCommand command) {
        writeRepository.delete(command.id());
        String message = messageResolver.getMessage("user.deleteByIdSuccess");
        return new DeleteUserByIdCommandResult(message);
    }
}
