package pl.bodzioch.damian.service;

import com.fasterxml.uuid.Generators;
import pl.bodzioch.damian.client.bur.BurServiceDto;
import pl.bodzioch.damian.coach.InnerCoach;
import pl.bodzioch.damian.customer.InnerCustomer;
import pl.bodzioch.damian.document.DocumentType;
import pl.bodzioch.damian.document.InnerDocument;
import pl.bodzioch.damian.infrastructure.database.*;
import pl.bodzioch.damian.intermediary.InnerIntermediary;
import pl.bodzioch.damian.operator.InnerOperator;
import pl.bodzioch.damian.program.InnerProgram;
import pl.bodzioch.damian.service.command_dto.CreateOrUpdateServiceCommand;
import pl.bodzioch.damian.service_provider.InnerServiceProvider;
import pl.bodzioch.damian.user.InnerUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static pl.bodzioch.damian.service.BadgeMessageType.*;
import static pl.bodzioch.damian.service.ServiceStatus.COMPLETED;
import static pl.bodzioch.damian.service.ServiceStatus.PUBLISHED;

//TODO general error handling on front which logging on db
record Service(
		@DbId
		@DbColumn(name = "srv_id")
        Long id,
		@DbColumn(name = "srv_uuid")
        UUID uuid,
		@DbColumn(name = "srv_version")
		Integer version,
		@DbColumn(name = "srv_bur_card_id")
        Long burCardId,
		@DbColumn(name = "srv_number")
		String number,
		@DbColumn(name = "srv_name")
        String name, //TODO usunąc indeks
		@DbColumn(name = "srv_type")
        ServiceType type,
		@DbColumn(name = "srv_start_date") //TODO walidować czy startDate nie jest po dacie endDate
		LocalDate startDate,
		@DbColumn(name = "srv_end_date")
		LocalDate endDate,
		@DbColumn(name = "srv_number_of_participants")
        Integer numberOfParticipants,
		@DbColumn(name = "srv_status")
		ServiceStatus status,
		@DbColumn(name = "srv_service_provider_id")
		Long serviceProviderId,
		@DbColumn(name = "srv_program_id")
        Long programId,
		@DbColumn(name = "srv_customer_id")
        Long customerId,
		@DbColumn(name = "srv_coach_ids")
        List<Long> coachIds,
		@DbColumn(name = "srv_intermediary_id")
        Long intermediaryId, //TODO poprawić pośrednik nie jest wymagany
		@DbColumn(name = "srv_created_at")
		LocalDateTime createdAt,
		@DbColumn(name = "srv_modified_at")
		LocalDateTime modifiedAt,
		@DbColumn(name = "srv_created_by")
		Long createdBy,
		@DbColumn(name = "srv_modified_by")
		Long modifiedBy,

		@DbManyToOne(prefix = "creator")
		InnerUser creator,
		@DbManyToOne(prefix = "modifier")
		InnerUser modifier,
		@DbManyToOne(prefix = "operator")
		InnerOperator operator,
		@DbManyToOne(prefix = "customer")
		InnerCustomer customer,
		@DbManyToOne(prefix = "service_provider")
		InnerServiceProvider serviceProvider,
		@DbManyToOne(prefix = "program")
		InnerProgram program,
		@DbManyToOne(prefix = "intermediary")
		InnerIntermediary intermediary,
		@DbOneToMany(prefix = "coach")
		List<InnerCoach> coaches, //TODO dodać walidacje min 2 trenerów na usługę
		@DbOneToMany(prefix = "document")
		List<InnerDocument> documents
) {
	@DbConstructor
	Service {
	}

	Service(CreateOrUpdateServiceCommand command, Long burCardId) {
		this(
				command.id(),
				Generators.timeBasedEpochGenerator().generate(),
				command.version(),
				burCardId,
				command.number(),
				command.name(),
				command.type(),
				command.startDate(),
				command.endDate(),
				command.numberOfParticipants(),
				command.status(),
				command.serviceProviderId(),
				command.programId(),
				command.customerId(),
				command.coachIds(),
				command.intermediaryId(),
				null, null,
				command.createdBy(),
				null, null, null, null, null, null, null,
				null, null, null
		);
	}

	Service(Service service, BurServiceDto burService) {
		this(
				service.id(), null, null, null, null, null, null, null,
				null, null, ServiceStatus.of(burService.status()),
				null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null,
				null, null, null
		);
	}

	List<BadgeMessage> getBadgeMessages() {
		ArrayList<BadgeMessage> messages = new ArrayList<>();
		if (hasNotCompleteStatusInBurAfterEndDate()) {
			messages.add(new BadgeMessage(NOT_COMPLETE_SERVICE));
		}
		if (this.status == PUBLISHED) {
			return messages;
		} else if (this.status == COMPLETED) {
			messages.addAll(validateDocumentsQuantity());
		}
		return messages;
	}

	private boolean hasNotCompleteStatusInBurAfterEndDate() {
		return this.status == PUBLISHED && LocalDate.now().isAfter(this.endDate);
	}

	private List<BadgeMessage> validateDocumentsQuantity() {
		return Arrays.stream(DocumentType.values())
				.map(item -> new AbstractMap.SimpleEntry<>(item, getDocumentsOfType(item)))
				.map(this::validateDocumentsQuantity)
				.flatMap(List::stream)
				.toList();
	}

	private List<InnerDocument> getDocumentsOfType(DocumentType type) {
		return this.documents.stream()
				.filter(item -> item.type() == type)
				.toList();
	}

	private List<BadgeMessage> validateDocumentsQuantity(AbstractMap.SimpleEntry<DocumentType, List<InnerDocument>> documentsOfTypes) {
		ArrayList<BadgeMessage> messages = new ArrayList<>();
		DocumentType documentType = documentsOfTypes.getKey();
		List<InnerDocument> documents = documentsOfTypes.getValue();
		List<Long> coachIdsWithInvoice = documents.stream().map(InnerDocument::coachId).toList();
		int numOfDoc = documents.size();

		if (documentType == DocumentType.REPORT && this.type == ServiceType.CONSULTING && numOfDoc != 1) {
			messages.add(new BadgeMessage(BadgeMessageType.MISSING_REPORT));
		} else if (documentType == DocumentType.CONSENT && numOfDoc != numberOfParticipants) {
			messages.add(new BadgeMessage(
					BadgeMessageType.NOT_ENOUGH_CONSENTS,
					List.of(this.numberOfParticipants.toString(), String.valueOf(numOfDoc)))
			);
		} else if (documentType == DocumentType.COACH_INVOICE && numOfDoc != this.coaches.size()) {
			this.coaches.stream()
					.filter(item -> !coachIdsWithInvoice.contains(item.id()))
					.map(item -> item.firstName() + " " + item.lastName())
					.map(item -> new BadgeMessage(BadgeMessageType.MISSING_COACH_INVOICE, List.of(item)))
					.forEach(messages::add);
		} else if (documentType == DocumentType.PROVIDER_INVOICE & numOfDoc != 1) {
			messages.add(new BadgeMessage(MISSING_PROVIDER_INVOICE));
		} else if (documentType == DocumentType.INTERMEDIARY_INVOICE && this.intermediaryId != null && numOfDoc != 1) {
			messages.add(new BadgeMessage(MISSING_INTERMEDIARY_INVOICE));
		} else if (documentType == DocumentType.PARTICIPANT_BUR_QUESTIONNAIRE && numOfDoc != this.numberOfParticipants) {
			messages.add(new BadgeMessage(
					NOT_ENOUGH_PARTICIPANT_BUR_QUESTIONNAIRES,
					List.of(this.numberOfParticipants.toString(), String.valueOf(numOfDoc))));
		} else if (documentType == DocumentType.CUSTOMER_BUR_QUESTIONNAIRE && numOfDoc != 1) {
			messages.add(new BadgeMessage(MISSING_CUSTOMER_BUR_QUESTIONNAIRE));
		} else if (documentType == DocumentType.PARTICIPANT_PROVIDER_QUESTIONNAIRE && numOfDoc != this.numberOfParticipants) {
			messages.add(new BadgeMessage(
					NOT_ENOUGH_PARTICIPANT_PROVIDER_QUESTIONNAIRE,
					List.of(this.numberOfParticipants.toString(), String.valueOf(numOfDoc))));
		} else if (documentType == DocumentType.ATTENDANCE_LIST && numOfDoc != 1) {
			messages.add(new BadgeMessage(MISSING_ATTENDANCE_LIST));
		} else if (documentType == DocumentType.PRESENTATION && numOfDoc < 1) {
			messages.add(new BadgeMessage(MISSING_PRESENTATION));
		}

		return messages;
	}
}
