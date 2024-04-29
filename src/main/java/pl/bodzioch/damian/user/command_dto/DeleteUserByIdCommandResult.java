package pl.bodzioch.damian.user.command_dto;

import pl.bodzioch.damian.infrastructure.command.CommandResult;

public record DeleteUserByIdCommandResult(
        String message
) implements CommandResult {
}
