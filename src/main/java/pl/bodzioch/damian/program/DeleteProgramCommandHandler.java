package pl.bodzioch.damian.program;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.program.command_dto.DeleteProgramCommand;
import pl.bodzioch.damian.program.command_dto.DeleteProgramCommandResult;
import pl.bodzioch.damian.utils.MessageResolver;

@CacheEvict(value = "programs", allEntries = true)
@Component
@RequiredArgsConstructor
class DeleteProgramCommandHandler implements CommandHandler<DeleteProgramCommand, DeleteProgramCommandResult> {

    private final IProgramWriteRepository writeRepository;
    private final MessageResolver messageResolver;


    @Override
    public Class<DeleteProgramCommand> commandClass() {
        return DeleteProgramCommand.class;
    }

    @Override
    public DeleteProgramCommandResult handle(DeleteProgramCommand command) {
        writeRepository.delete(command.id());
        String message = messageResolver.getMessage("program.deleteByIdSuccess");
        return new DeleteProgramCommandResult(message);
    }
}
