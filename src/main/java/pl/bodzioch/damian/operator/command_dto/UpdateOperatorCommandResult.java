package pl.bodzioch.damian.operator.command_dto;

import pl.bodzioch.damian.infrastructure.command.CommandResult;

public record UpdateOperatorCommandResult(
		String message
) implements CommandResult {
}
