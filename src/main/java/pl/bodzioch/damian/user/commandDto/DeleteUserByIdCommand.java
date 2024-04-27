package pl.bodzioch.damian.user.commandDto;

import pl.bodzioch.damian.infrastructure.command.Command;

public record DeleteUserByIdCommand(
        Long id
) implements Command<DeleteUserByIdCommandResult> {
}
