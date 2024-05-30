package pl.bodzioch.damian.coach;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.coach.command_dto.DeleteCoachCommand;
import pl.bodzioch.damian.coach.command_dto.DeleteCoachCommandResult;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.utils.MessageResolver;

@Component
@RequiredArgsConstructor
class DeleteCoachCommandHandler implements CommandHandler<DeleteCoachCommand, DeleteCoachCommandResult> {

    private final ICoachWriteRepository writeRepository;
    private final MessageResolver messageResolver;

    @Override
    public Class<DeleteCoachCommand> commandClass() {
        return DeleteCoachCommand.class;
    }

    @Override
    @CacheEvict(value = "coaches", allEntries = true)
    public DeleteCoachCommandResult handle(DeleteCoachCommand command) {
        writeRepository.delete(command.id());
        String message = messageResolver.getMessage("coach.deleteByIdSuccess");
        return new DeleteCoachCommandResult(message);
    }
}
