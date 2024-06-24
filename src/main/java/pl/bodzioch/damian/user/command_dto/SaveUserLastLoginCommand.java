package pl.bodzioch.damian.user.command_dto;

import pl.bodzioch.damian.infrastructure.command.Command;

public record SaveUserLastLoginCommand(
        Long userId
) implements Command<SaveUserLastLoginCommandResult> {
}
