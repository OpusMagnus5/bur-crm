package pl.bodzioch.damian.intermediary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.intermediary.command_dto.UpdateIntermediaryCommand;
import pl.bodzioch.damian.intermediary.command_dto.UpdateIntermediaryCommandResult;
import pl.bodzioch.damian.utils.MessageResolver;

@Component
@RequiredArgsConstructor
class UpdateIntermediaryCommandHandler implements CommandHandler<UpdateIntermediaryCommand, UpdateIntermediaryCommandResult> {

	private final IIntermediaryWriteRepository writeRepository;
	private final MessageResolver messageResolver;

	@Override
	public Class<UpdateIntermediaryCommand> commandClass() {
		return UpdateIntermediaryCommand.class;
	}

	@Override
	public UpdateIntermediaryCommandResult handle(UpdateIntermediaryCommand command) {
		writeRepository.update(new Intermediary(command));
		String message = messageResolver.getMessage("intermediary.updateSuccess");
		return new UpdateIntermediaryCommandResult(message);
	}
}
