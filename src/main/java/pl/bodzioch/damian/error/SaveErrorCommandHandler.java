package pl.bodzioch.damian.error;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.error.command_dto.SaveErrorCommand;
import pl.bodzioch.damian.error.command_dto.SaveErrorCommandResult;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;

@Component
@RequiredArgsConstructor
class SaveErrorCommandHandler implements CommandHandler<SaveErrorCommand, SaveErrorCommandResult> {

    private final IErrorWriteRepository writeRepository;

    @Override
    public Class<SaveErrorCommand> commandClass() {
        return SaveErrorCommand.class;
    }

    @Override
    public SaveErrorCommandResult handle(SaveErrorCommand command) {
        writeRepository.saveError(new Error(command.error()));
        return new SaveErrorCommandResult();
    }
}
