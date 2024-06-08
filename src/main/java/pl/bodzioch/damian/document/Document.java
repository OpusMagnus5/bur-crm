package pl.bodzioch.damian.document;

import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbId;

import java.time.LocalDateTime;
import java.util.UUID;

record Document(
        @DbId
        @DbColumn(name = "doc_id")
        Long id,
        @DbColumn(name = "doc_uuid")
        UUID uuid,
        @DbColumn(name = "doc_version")
        Integer version,
        @DbColumn(name = "doc_service_id")
        Long serviceId,
        @DbColumn(name = "doc_coach_id")
        Long coachId,
        @DbColumn(name = "doc_type")
        DocumentType type,
        @DbColumn(name = "doc_file_name")
        String fileName,
        @DbColumn(name = "doc_file_path")
        String filePath,
        @DbColumn(name = "doc_file_extension")
        String fileExtension,
        @DbColumn(name = "doc_created_at")
        LocalDateTime createdAt,
        @DbColumn(name = "doc_modified_at")
        LocalDateTime modifiedAt,
        @DbColumn(name = "doc_created_by")
        Long createdBy,
        @DbColumn(name = "doc_modified_by")
        Long modifiedBy
) {
}