package pl.bodzioch.damian.service.command_dto;

import pl.bodzioch.damian.dto.CreateNewServiceRequest;
import pl.bodzioch.damian.infrastructure.command.Command;
import pl.bodzioch.damian.service.ServiceType;
import pl.bodzioch.damian.utils.CipherComponent;

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

	public CreateNewServiceCommand(CreateNewServiceRequest request, CipherComponent cipher) {
		this(
				request.number(),
				request.name(),
				ServiceType.valueOf(request.type()),
				request.startDate(),
				request.endDate(),
				request.numberOfParticipants(),
				Long.parseLong(cipher.decryptMessage(request.serviceProviderId())),
				Long.parseLong(cipher.decryptMessage(request.programId())),
				Long.parseLong(cipher.decryptMessage(request.customerId())),
				request.coachIds().stream()
						.map(cipher::decryptMessage)
						.map(Long::parseLong)
						.toList(),
				Long.parseLong(cipher.decryptMessage(request.intermediaryId())),
				1L//TODO poprawić
		);
	}
}
