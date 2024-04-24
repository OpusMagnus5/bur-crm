package pl.bodzioch.damian.user.commandDto;

import pl.bodzioch.damian.infrastructure.command.CommandResult;

public record CreateNewUserCommandResult(
        String login,
        String password

) implements CommandResult {
}
