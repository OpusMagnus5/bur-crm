package pl.bodzioch.damian.service.command_dto;

import pl.bodzioch.damian.infrastructure.command.Command;
import pl.bodzioch.damian.service.ServiceType;

import java.time.LocalDate;
import java.util.List;

public record CreateNewServiceCommand(
		String number,
		String name,
		ServiceType type,
		LocalDate startDate,
		LocalDate endDate,
		Integer numberOfParticipants,
		Long serviceProviderId,
		Long programId,
		Long customerId,
		List<Long> coachIds,
		Long intermediaryId,
		Long createdBy
) implements Command<CreateNewServiceCommandResult> {
}
