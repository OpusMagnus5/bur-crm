package pl.bodzioch.damian.service.command_dto;

import pl.bodzioch.damian.infrastructure.command.Command;

public record DeleteServiceCommand(
        Long id
) implements Command<DeleteServiceCommandResult> {
}
