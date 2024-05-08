package pl.bodzioch.damian.operator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.operator.command_dto.UpdateOperatorCommand;
import pl.bodzioch.damian.operator.command_dto.UpdateOperatorCommandResult;
import pl.bodzioch.damian.utils.MessageResolver;

@Component
@RequiredArgsConstructor
class UpdateOperatorCommandHandler implements CommandHandler<UpdateOperatorCommand, UpdateOperatorCommandResult> {

	private final IOperatorWriteRepository writeRepository;
	private final MessageResolver messageResolver;

	@Override
	public Class<UpdateOperatorCommand> commandClass() {
		return UpdateOperatorCommand.class;
	}

	@Override
	public UpdateOperatorCommandResult handle(UpdateOperatorCommand command) {
		writeRepository.update(new Operator(command));
		String message = messageResolver.getMessage("operator.updateSuccess");
		return new UpdateOperatorCommandResult(message);
	}
}
