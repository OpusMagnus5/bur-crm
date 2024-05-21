package pl.bodzioch.damian.coach;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.coach.command_dto.UpdateCoachCommand;
import pl.bodzioch.damian.coach.command_dto.UpdateCoachCommandResult;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.utils.MessageResolver;

@Component
@RequiredArgsConstructor
class UpdateCoachCommandHandler implements CommandHandler<UpdateCoachCommand, UpdateCoachCommandResult> {

    private final ICoachWriteRepository writeRepository;
    private final MessageResolver messageResolver;

    @Override
    public Class<UpdateCoachCommand> commandClass() {
        return UpdateCoachCommand.class;
    }

    @Override
    public UpdateCoachCommandResult handle(UpdateCoachCommand command) {
       writeRepository.update(new Coach(command));
        String message = messageResolver.getMessage("coach.updateSuccess");
        return new UpdateCoachCommandResult(message);
    }
}
