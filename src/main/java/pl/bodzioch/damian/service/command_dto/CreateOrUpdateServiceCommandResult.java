package pl.bodzioch.damian.service.command_dto;

import pl.bodzioch.damian.infrastructure.command.CommandResult;

import java.util.List;

public record CreateOrUpdateServiceCommandResult(
		List<String> messages
) implements CommandResult {
}
