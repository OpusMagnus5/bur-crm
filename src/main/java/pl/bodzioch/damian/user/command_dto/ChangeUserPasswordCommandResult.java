package pl.bodzioch.damian.user.command_dto;

import pl.bodzioch.damian.infrastructure.command.CommandResult;

public record ChangeUserPasswordCommandResult(
        String message
) implements CommandResult {
}
