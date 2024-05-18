package pl.bodzioch.damian.coach.command_dto;

import pl.bodzioch.damian.infrastructure.command.CommandResult;

public record CreateNewCoachCommandResult(
        String message
) implements CommandResult {
}
