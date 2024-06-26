package pl.bodzioch.damian.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.user.command_dto.ResetUserPasswordCommand;
import pl.bodzioch.damian.user.command_dto.ResetUserPasswordCommandResult;
import pl.bodzioch.damian.utils.PermissionService;

@Component
@RequiredArgsConstructor
class ResetUserPasswordCommandHandler implements CommandHandler<ResetUserPasswordCommand, ResetUserPasswordCommandResult> {

    private final IUserWriteRepository writeRepository;
    private final IUserReadRepository readRepository;
    private final PermissionService permissionService;

    @Override
    public Class<ResetUserPasswordCommand> commandClass() {
        return ResetUserPasswordCommand.class;
    }

    @Override
    public ResetUserPasswordCommandResult handle(ResetUserPasswordCommand command) {
        User requestedUser = readRepository.getById(command.id()).orElseThrow(() -> User.buildUserByIdNotFound(command.id()));
        if (!permissionService.hasTheSameRoleOrHigher(requestedUser.roles())) {
            throw new AccessDeniedException("Access Denied to reset user password!");
        }
        String newPassword = User.generateNewPassword();
        User user = new User(command, newPassword);
        writeRepository.changePassword(user);
        return new ResetUserPasswordCommandResult(newPassword);
    }
}
