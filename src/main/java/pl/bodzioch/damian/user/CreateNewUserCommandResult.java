package pl.bodzioch.damian.user;

import pl.bodzioch.damian.configuration.command.CommandResult;

public record CreateNewUserCommandResult(
        String login,
        String password

) implements CommandResult {
}
