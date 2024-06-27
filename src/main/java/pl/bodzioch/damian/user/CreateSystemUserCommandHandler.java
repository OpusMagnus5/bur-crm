package pl.bodzioch.damian.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.user.command_dto.CreateSystemUserCommand;
import pl.bodzioch.damian.user.command_dto.CreateSystemUserCommandResult;

@Component
@RequiredArgsConstructor
class CreateSystemUserCommandHandler implements CommandHandler<CreateSystemUserCommand, CreateSystemUserCommandResult> {

    private final IUserWriteRepository writeRepository;

    @Override
    public Class<CreateSystemUserCommand> commandClass() {
        return null;
    }

    @Override
    public CreateSystemUserCommandResult handle(CreateSystemUserCommand command) {
        return null;
    }
}
