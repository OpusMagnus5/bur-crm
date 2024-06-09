package pl.bodzioch.damian.document.command_dto;

import pl.bodzioch.damian.document.DocumentType;

public record AddNewDocumentsCommandData(
        DocumentType type,
        String fileName,
        String fileExtension,
        Long serviceId,
        Long coachId,
        byte[] fileData,
        Long creatorId //TODO poprawić
) {
}
