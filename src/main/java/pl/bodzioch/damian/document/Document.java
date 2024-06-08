package pl.bodzioch.damian.document;

import com.fasterxml.uuid.Generators;
import org.springframework.http.HttpStatus;
import pl.bodzioch.damian.document.command_dto.AddNewDocumentCommand;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbConstructor;
import pl.bodzioch.damian.infrastructure.database.DbId;
import pl.bodzioch.damian.infrastructure.database.DbManyToOne;
import pl.bodzioch.damian.service.InnerService;
import pl.bodzioch.damian.value_object.ErrorData;

import java.time.LocalDateTime;
import java.util.List;
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
        @DbColumn(name = "doc_file_extension")
        String fileExtension,
        @DbColumn(name = "doc_created_at")
        LocalDateTime createdAt,
        @DbColumn(name = "doc_modified_at")
        LocalDateTime modifiedAt,
        @DbColumn(name = "doc_created_by")
        Long createdBy,
        @DbColumn(name = "doc_modified_by")
        Long modifiedBy,

        @DbManyToOne(prefix = "service")
        InnerService service
) {
        @DbConstructor
        Document {
        }

        Document(AddNewDocumentCommand command, List<Document> serviceDocuments) {
                this(
                        null, Generators.timeBasedEpochGenerator().generate(), null, command.serviceId(),
                        command.coachId(), command.type(), command.fileExtension(), null, null,
                        command.creatorId(), null, null
                );
                canBeAdded(serviceDocuments);
        }

        void canBeAdded(List<Document> serviceDocuments) {
                if (this.type == DocumentType.REPORT) {
                        if (hasTypeOf(serviceDocuments, type)) {
                                throw buildValidationException("error.client.service.reportAlreadyExists", List.of());
                        }
                }
        }

        private boolean hasTypeOf(List<Document> documents, DocumentType type) {
                return documents.stream()
                        .map(Document::type)
                        .anyMatch(item -> type == item);
        }

        private AppException buildValidationException(String message, List<String> params) {
                return new AppException(
                        HttpStatus.BAD_REQUEST,
                        List.of(new ErrorData(message, params))
                );
        }
}