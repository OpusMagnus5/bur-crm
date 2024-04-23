package pl.bodzioch.damian.user.commandDto;

import pl.bodzioch.damian.infrastructure.command.Command;

public record GetAllRolesCommand() implements Command<GetAllRolesCommandResult> {
}
