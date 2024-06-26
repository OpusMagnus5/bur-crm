package pl.bodzioch.damian.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.user.command_dto.DeleteUserByIdCommand;
import pl.bodzioch.damian.user.command_dto.DeleteUserByIdCommandResult;
import pl.bodzioch.damian.utils.MessageResolver;
import pl.bodzioch.damian.utils.PermissionService;

@Component
@RequiredArgsConstructor
class DeleteUserByIdCommandHandler implements CommandHandler<DeleteUserByIdCommand, DeleteUserByIdCommandResult> {

    private final MessageResolver messageResolver;
    private final IUserWriteRepository writeRepository;
    private final IUserReadRepository readRepository;
    private final PermissionService permissionService;

    @Override
    public Class<DeleteUserByIdCommand> commandClass() {
        return DeleteUserByIdCommand.class;
    }

    @Override
    public DeleteUserByIdCommandResult handle(DeleteUserByIdCommand command) {
        User user = readRepository.getById(command.id()).orElseThrow(() -> User.buildUserByIdNotFound(command.id()));
        if (!permissionService.hasTheSameRoleOrHigher(user.roles())) {
            throw new AccessDeniedException("Access Denied to delete user!");
        }
        writeRepository.delete(command.id());
        String message = messageResolver.getMessage("user.deleteByIdSuccess");
        return new DeleteUserByIdCommandResult(message);
    }
}
