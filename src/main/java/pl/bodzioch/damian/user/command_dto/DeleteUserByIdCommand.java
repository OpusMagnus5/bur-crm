package pl.bodzioch.damian.user.command_dto;

import pl.bodzioch.damian.infrastructure.command.Command;

public record DeleteUserByIdCommand(
        Long id
) implements Command<DeleteUserByIdCommandResult> {
}
