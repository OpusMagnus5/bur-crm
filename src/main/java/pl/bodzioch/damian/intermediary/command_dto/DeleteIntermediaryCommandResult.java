package pl.bodzioch.damian.intermediary.command_dto;

import pl.bodzioch.damian.infrastructure.command.CommandResult;

public record DeleteIntermediaryCommandResult(
        String message
) implements CommandResult {
}
