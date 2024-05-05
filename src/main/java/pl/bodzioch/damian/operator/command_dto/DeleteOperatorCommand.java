package pl.bodzioch.damian.operator.command_dto;

import pl.bodzioch.damian.infrastructure.command.Command;

public record DeleteOperatorCommand(
        Long id

) implements Command<DeleteOperatorCommandResult> {
}
