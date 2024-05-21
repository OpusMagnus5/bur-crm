package pl.bodzioch.damian.coach.command_dto;

import pl.bodzioch.damian.infrastructure.command.CommandResult;

public record UpdateCoachCommandResult(
        String message
) implements CommandResult {
}
