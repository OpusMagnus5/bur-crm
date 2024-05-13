package pl.bodzioch.damian.program.command_dto;

import pl.bodzioch.damian.infrastructure.command.CommandResult;

public record DeleteProgramCommandResult(
        String message
) implements CommandResult {
}
