package pl.bodzioch.damian.error.command_dto;

import pl.bodzioch.damian.error.ErrorDto;
import pl.bodzioch.damian.infrastructure.command.Command;

public record SaveErrorCommand(
        ErrorDto error
) implements Command<SaveErrorCommandResult> {
}
