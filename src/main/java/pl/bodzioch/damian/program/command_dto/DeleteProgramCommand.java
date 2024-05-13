package pl.bodzioch.damian.program.command_dto;

import pl.bodzioch.damian.infrastructure.command.Command;

public record DeleteProgramCommand(
        Long id
) implements Command<DeleteProgramCommandResult> {
}
