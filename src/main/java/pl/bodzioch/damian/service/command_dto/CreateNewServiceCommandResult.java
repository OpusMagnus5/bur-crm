package pl.bodzioch.damian.service.command_dto;

import pl.bodzioch.damian.infrastructure.command.CommandResult;

import java.util.List;

public record CreateNewServiceCommandResult(
		List<String> message
) implements CommandResult {
}
