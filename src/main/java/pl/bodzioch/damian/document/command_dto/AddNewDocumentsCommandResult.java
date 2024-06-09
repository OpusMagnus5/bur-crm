package pl.bodzioch.damian.document.command_dto;

import pl.bodzioch.damian.infrastructure.command.CommandResult;

public record AddNewDocumentsCommandResult(
        String message
) implements CommandResult {
}
