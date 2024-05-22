package pl.bodzioch.damian.intermediary.command_dto;

import pl.bodzioch.damian.infrastructure.command.Command;

public record DeleteIntermediaryCommand(
        Long id
) implements Command<DeleteIntermediaryCommandResult> {
}
