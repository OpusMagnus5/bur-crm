package pl.bodzioch.damian.document;

import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbConstructor;
import pl.bodzioch.damian.infrastructure.database.DbId;
import pl.bodzioch.damian.infrastructure.database.DbManyToOne;
import pl.bodzioch.damian.user.InnerUser;

import java.time.LocalDateTime;

public record InnerDocument(
        @DbId
        @DbColumn(name = "doc_id")
        Long id,
        @DbColumn(name = "doc_coach_id")
        Long coachId,
        @DbColumn(name = "doc_type")
        DocumentType type,
        @DbColumn(name = "doc_file_name")
        String fileName,
        @DbColumn(name = "doc_file_extension")
        String fileExtension,
        @DbColumn(name = "doc_created_at")
        LocalDateTime createdAt,

        @DbManyToOne(prefix = "creator")
        InnerUser creator
) {
        @DbConstructor
        public InnerDocument {
        }
}