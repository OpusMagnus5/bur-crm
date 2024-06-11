package pl.bodzioch.damian.document;

import pl.bodzioch.damian.user.InnerUserDto;

import java.time.LocalDateTime;

public record InnerDocumentDto(
        Long id,
        Long coachId,
        DocumentType type,
        String fileName,
        String fileExtension,
        LocalDateTime createdAt,
        InnerUserDto creator
) {

    public InnerDocumentDto(InnerDocument document) {
        this(
                document.id(),
                document.coachId(),
                document.type(),
                document.fileName(),
                document.fileExtension(),
                document.createdAt(),
                new InnerUserDto(document.creator())
        );
    }
}