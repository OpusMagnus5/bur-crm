package pl.bodzioch.damian.user.commandDto;

import pl.bodzioch.damian.infrastructure.command.CommandResult;

public record DeleteUserByIdCommandResult(
        String message
) implements CommandResult {
}
