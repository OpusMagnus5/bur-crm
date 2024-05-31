package pl.bodzioch.damian.service;

import com.fasterxml.uuid.Generators;
import pl.bodzioch.damian.customer.InnerCustomer;
import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbConstructor;
import pl.bodzioch.damian.infrastructure.database.DbId;
import pl.bodzioch.damian.infrastructure.database.DbManyToOne;
import pl.bodzioch.damian.operator.InnerOperator;
import pl.bodzioch.damian.service.command_dto.CreateNewServiceCommand;
import pl.bodzioch.damian.service_provider.InnerServiceProvider;
import pl.bodzioch.damian.user.InnerUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

//TODO general error handling on front which logging on db
record Service(
		@DbId
		@DbColumn(name = "srv_id")
        Long id,
		@DbColumn(name = "srv_uuid")
        UUID uuid,
		@DbColumn(name = "srv_bur_card_id")
        Long burCardId,
		@DbColumn(name = "srv_number")
		String number,
		@DbColumn(name = "srv_name")
        String name,
		@DbColumn(name = "srv_type")
        ServiceType type,
		@DbColumn(name = "srv_start_date")
		LocalDate startDate,
		@DbColumn(name = "srv_end_date")
		LocalDate endDate,
		@DbColumn(name = "srv_number_of_participants")
        Integer numberOfParticipants,
		@DbColumn(name = "srv_service_provider_id")
		Long serviceProviderId,
		@DbColumn(name = "srv_program_id")
        Long programId,
		@DbColumn(name = "srv_customer_id")
        Long customerId,
		@DbColumn(name = "srv_coach_ids")
        List<Long> coachIds,
		@DbColumn(name = "srv_intermediary_id")
        Long intermediaryId,
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
		InnerServiceProvider serviceProvider
) {
	@DbConstructor
	Service {
	}

	Service(CreateNewServiceCommand command, Long burCardId) {
		this(
				null,
				Generators.timeBasedEpochGenerator().generate(),
				burCardId,
				command.number(),
				command.name(),
				command.type(),
				command.startDate(),
				command.endDate(),
				command.numberOfParticipants(),
				command.serviceProviderId(),
				command.programId(),
				command.customerId(),
				command.coachIds(),
				command.intermediaryId(),
				null,
				null,
				command.createdBy(),
				null,
				null,
				null,
				null,
				null,
				null
		);
	}
}
