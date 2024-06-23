package pl.bodzioch.damian.user.command_dto;

import pl.bodzioch.damian.infrastructure.command.CommandResult;

public record ResetUserPasswordCommandResult(
        String newPassword
) implements CommandResult {

    @Override
    public String toString() {
        return "ResetUserPasswordCommandResult{}";
    }
}
