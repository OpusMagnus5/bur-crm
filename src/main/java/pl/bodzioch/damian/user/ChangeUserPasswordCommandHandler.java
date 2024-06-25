package pl.bodzioch.damian.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.user.command_dto.ChangeUserPasswordCommand;
import pl.bodzioch.damian.user.command_dto.ChangeUserPasswordCommandResult;
import pl.bodzioch.damian.utils.MessageResolver;

@Component
@RequiredArgsConstructor
class ChangeUserPasswordCommandHandler implements CommandHandler<ChangeUserPasswordCommand, ChangeUserPasswordCommandResult> {

    private final IUserWriteRepository writeRepository;
    private final MessageResolver messageResolver;

    @Override
    public Class<ChangeUserPasswordCommand> commandClass() {
        return ChangeUserPasswordCommand.class;
    }

    @Override
    public ChangeUserPasswordCommandResult handle(ChangeUserPasswordCommand command) {
        User user = new User(command);
        writeRepository.changePassword(user);
        String message = messageResolver.getMessage("user.changePasswordSuccess");
        return new ChangeUserPasswordCommandResult(message);
    }
}
