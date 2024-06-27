package pl.bodzioch.damian.user.command_dto;

import pl.bodzioch.damian.infrastructure.command.Command;
import pl.bodzioch.damian.user.UserRole;

public record CreateSystemUserCommand(
    Long id,
    String password,
    String email,
    String firstName,
    String lastName,
    UserRole role,
    Long creatorId
) implements Command<CreateSystemUserCommandResult> {
}
