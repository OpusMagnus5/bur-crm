package pl.bodzioch.damian.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.user.command_dto.SaveUserLastLoginCommand;
import pl.bodzioch.damian.user.command_dto.SaveUserLastLoginCommandResult;

@Component
@RequiredArgsConstructor
class SaveUserLastLoginCommandHandler implements CommandHandler<SaveUserLastLoginCommand, SaveUserLastLoginCommandResult> {

    private final IUserWriteRepository writeRepository;

    @Override
    public Class<SaveUserLastLoginCommand> commandClass() {
        return SaveUserLastLoginCommand.class;
    }

    @Override
    public SaveUserLastLoginCommandResult handle(SaveUserLastLoginCommand command) {
        writeRepository.setLastLogin(command.userId());
        return new SaveUserLastLoginCommandResult();
    }
}
