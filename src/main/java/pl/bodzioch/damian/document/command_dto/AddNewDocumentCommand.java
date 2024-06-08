package pl.bodzioch.damian.document.command_dto;

import pl.bodzioch.damian.document.DocumentType;
import pl.bodzioch.damian.infrastructure.command.Command;

public record AddNewDocumentCommand(
        DocumentType type,
        String fileExtension,
        Long serviceId,
        Long coachId,
        Long creatorId //TODO poprawić
) implements Command<AddNewDocumentCommandResult> {
}
