package pl.bodzioch.damian.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.user.command_dto.ResetUserPasswordCommand;
import pl.bodzioch.damian.user.command_dto.ResetUserPasswordCommandResult;

@Component
@RequiredArgsConstructor
class ResetUserPasswordCommandHandler implements CommandHandler<ResetUserPasswordCommand, ResetUserPasswordCommandResult> {

    private final IUserWriteRepository writeRepository;

    @Override
    public Class<ResetUserPasswordCommand> commandClass() {
        return ResetUserPasswordCommand.class;
    }

    @Override
    public ResetUserPasswordCommandResult handle(ResetUserPasswordCommand command) {
        String newPassword = User.generateNewPassword();
        User user = new User(command, newPassword);
        writeRepository.changePassword(user);
        return new ResetUserPasswordCommandResult(newPassword);
    }
}
