package pl.bodzioch.damian.service.command_dto;

import pl.bodzioch.damian.infrastructure.command.CommandResult;

public record DeleteServiceCommandResult(
        String message
) implements CommandResult {
}
