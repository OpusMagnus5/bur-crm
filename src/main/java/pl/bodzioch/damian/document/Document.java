package pl.bodzioch.damian.document;

import com.fasterxml.uuid.Generators;
import org.springframework.http.HttpStatus;
import pl.bodzioch.damian.document.command_dto.AddNewDocumentsCommandData;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbConstructor;
import pl.bodzioch.damian.infrastructure.database.DbId;
import pl.bodzioch.damian.infrastructure.database.DbManyToOne;
import pl.bodzioch.damian.service.InnerService;
import pl.bodzioch.damian.service.ServiceType;
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
        @DbColumn(name = "doc_file_name")
        String fileName,
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
        byte[] fileData,

        @DbManyToOne(prefix = "service")
        InnerService service
) {
        @DbConstructor
        Document {
        }

        Document(AddNewDocumentsCommandData command) {
                this(
                        null, Generators.timeBasedEpochGenerator().generate(), null, command.serviceId(),
                        command.coachId(), command.type(), command.fileName(), command.fileExtension(), null,
                        null, command.creatorId(), null, command.fileData(), null
                );
        }

        static List<Document> newDocuments(List<AddNewDocumentsCommandData> commandData, List<Document> serviceDocuments) {
                ServiceType serviceType = serviceDocuments.getFirst().service().type();
                DocumentType documentType = commandData.getFirst().type();

                validateSameDocumentType(commandData);
                validateQuantity(commandData, serviceDocuments);
                validateCanBeAdded(serviceType, documentType);

                return commandData.stream()
                        .map(Document::new)
                        .toList();
        }

        private static void validateSameDocumentType(List<AddNewDocumentsCommandData> commandData) {
                boolean hasSameType = commandData.stream()
                        .allMatch(item -> item.type() == commandData.getFirst().type());
                if (!hasSameType) {
                        throw buildValidationException("notSameType", List.of());
                }
        }

        private static void validateQuantity(List<AddNewDocumentsCommandData> commandData, List<Document> serviceDocuments) {
                DocumentType documentType = commandData.getFirst().type();
                int numOfServiceDocument = getNumberOdServiceDocuments(serviceDocuments);
                int documentSum = commandData.size() + numOfServiceDocument;
                int numberOfParticipants = serviceDocuments.getFirst().service().numberOfParticipants();


                if (documentType == DocumentType.REPORT && documentSum > 1) {
                        throw buildValidationException("maxNumberOfReport", List.of());
                } else if (documentType == DocumentType.CONSENT && documentSum > numberOfParticipants) {
                        throw buildValidationException("maxNumberOfConsent",
                                List.of(String.valueOf(numberOfParticipants), String.valueOf(numOfServiceDocument)));
                } else if (documentType == DocumentType.COACH_INVOICE && documentSum > 1) {
                        throw buildValidationException("maxNumberOfCoachInvoice", List.of());
                } else if (documentType == DocumentType.PROVIDER_INVOICE && documentSum > 1) {
                        throw buildValidationException("maxNumberOfProviderInvoice", List.of());
                } else if (documentType == DocumentType.INTERMEDIARY_INVOICE && documentSum > 1) {
                        throw buildValidationException("maxNumberOfIntermediaryInvoice", List.of());
                } else if (documentType == DocumentType.PARTICIPANT_BUR_QUESTIONNAIRE && documentSum > numberOfParticipants) {
                        throw buildValidationException("maxNumberOfParticipantBurQuestionnaire",
                                List.of(String.valueOf(numberOfParticipants), String.valueOf(numOfServiceDocument)));
                } else if (documentType == DocumentType.CUSTOMER_BUR_QUESTIONNAIRE && documentSum > 1) {
                        throw buildValidationException("maxNumberOfCustomerBurQuestionnaire", List.of());
                } else if (documentType == DocumentType.PARTICIPANT_PROVIDER_QUESTIONNAIRE && documentSum > numberOfParticipants) {
                        throw buildValidationException("maxNumberOfParticipantProviderQuestionnaire",
                                List.of(String.valueOf(numberOfParticipants), String.valueOf(numOfServiceDocument)));
                } else if (documentType == DocumentType.ATTENDANCE_LIST && documentSum > 1) {
                        throw buildValidationException("maxNumberOfAttendanceList", List.of());
                }
        }

        private static int getNumberOdServiceDocuments(List<Document> serviceDocuments) {
                return serviceDocuments.stream()
                        .filter(item -> item.type != null)
                        .toList().size();
        }

        private static void validateCanBeAdded(ServiceType serviceType, DocumentType documentType) {
                if (documentType == DocumentType.REPORT) {
                        if (serviceType != ServiceType.CONSULTING) {
                                throw buildValidationException("wrongServiceTypeForReport", List.of());
                        }
                }
        }

        private static AppException buildValidationException(String message, List<String> params) {
                return new AppException(
                        HttpStatus.BAD_REQUEST,
                        List.of(new ErrorData("error.client.document." + message, params))
                );
        }
}