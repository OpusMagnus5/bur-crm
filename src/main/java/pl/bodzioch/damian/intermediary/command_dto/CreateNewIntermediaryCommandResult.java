package pl.bodzioch.damian.intermediary.command_dto;

import pl.bodzioch.damian.infrastructure.command.CommandResult;

public record CreateNewIntermediaryCommandResult(
        String message
) implements CommandResult {
}
