package pl.bodzioch.damian.program;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.program.command_dto.UpdateProgramCommand;
import pl.bodzioch.damian.program.command_dto.UpdateProgramCommandResult;
import pl.bodzioch.damian.utils.MessageResolver;

@CacheEvict(value = "programs", allEntries = true)
@Component
@RequiredArgsConstructor
class UpdateProgramCommandHandler implements CommandHandler<UpdateProgramCommand, UpdateProgramCommandResult> {

	private final IProgramWriteRepository writeRepository;
	private final MessageResolver messageResolver;

	@Override
	public Class<UpdateProgramCommand> commandClass() {
		return UpdateProgramCommand.class;
	}

	@Override
	public UpdateProgramCommandResult handle(UpdateProgramCommand command) {
		writeRepository.update(new Program(command));
		String message = messageResolver.getMessage("program.updateSuccess");
		return new UpdateProgramCommandResult(message);
	}
}
