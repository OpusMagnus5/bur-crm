package pl.bodzioch.damian.document;

import java.time.LocalDateTime;
import java.util.UUID;

public record DocumentDto(
        Long id,
        UUID uuid,
        Integer version,
        Long serviceId,
        Long coachId,
        DocumentType type,
        String fileExtension,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Long createdBy,
        Long modifiedBy
) {
}