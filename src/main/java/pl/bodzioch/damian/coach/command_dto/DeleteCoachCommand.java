package pl.bodzioch.damian.coach.command_dto;

import pl.bodzioch.damian.infrastructure.command.Command;

public record DeleteCoachCommand(
        Long id
) implements Command<DeleteCoachCommandResult> {
}
