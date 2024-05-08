package pl.bodzioch.damian.operator.command_dto;

import pl.bodzioch.damian.infrastructure.command.Command;

public record UpdateOperatorCommand(
		Long id,
		Integer version,
		String name,
		String notes,
		Long modifiedBy //TODO poprawic
) implements Command<UpdateOperatorCommandResult> {
}
