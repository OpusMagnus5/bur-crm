package pl.bodzioch.damian.user.command_dto;

import pl.bodzioch.damian.infrastructure.command.CommandResult;

public record CreateNewOrUpdateUserCommandResult(
        String login,
        String password,
        String message

) implements CommandResult {
}
