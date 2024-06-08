package pl.bodzioch.damian.document.command_dto;

import pl.bodzioch.damian.document.DocumentType;
import pl.bodzioch.damian.infrastructure.command.CommandResult;

public record AddNewDocumentCommandResult(
        Long id,
        String fileName,
        String extension,
        String message,
        DocumentType type
) implements CommandResult {
}
