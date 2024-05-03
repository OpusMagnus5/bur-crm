package pl.bodzioch.damian.operator.command_dto;

import pl.bodzioch.damian.infrastructure.command.CommandResult;

public record CreateNewOperatorCommandResult(
        String message
) implements CommandResult {
}
