package pl.bodzioch.damian.service.command_dto;

import org.apache.commons.lang3.StringUtils;
import pl.bodzioch.damian.dto.CreateOrUpdateServiceRequest;
import pl.bodzioch.damian.infrastructure.command.Command;
import pl.bodzioch.damian.service.ServiceStatus;
import pl.bodzioch.damian.service.ServiceType;
import pl.bodzioch.damian.utils.CipherComponent;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record CreateOrUpdateServiceCommand(
		Long id,
		Integer version,
		String number,
		String name,
		ServiceType type,
		LocalDate startDate,
		LocalDate endDate,
		Integer numberOfParticipants,
		ServiceStatus status,
		Long serviceProviderId,
		Long programId,
		Long customerId,
		List<Long> coachIds,
		Long intermediaryId,
		Long createdBy
) implements Command<CreateOrUpdateServiceCommandResult> {

	public CreateOrUpdateServiceCommand(CreateOrUpdateServiceRequest request, CipherComponent cipher) {
		this(
				Optional.ofNullable(request.id())
						.map(cipher::decryptMessage)
						.map(Long::parseLong)
						.orElse(null),
				request.version(),
				request.number(),
				request.name(),
				ServiceType.valueOf(request.type()),
				request.startDate(),
				request.endDate(),
				request.numberOfParticipants(),
				ServiceStatus.valueOf(request.status()),
				Long.parseLong(cipher.decryptMessage(request.serviceProviderId())),
				Long.parseLong(cipher.decryptMessage(request.programId())),
				Long.parseLong(cipher.decryptMessage(request.customerId())),
				request.coachIds().stream()
						.map(cipher::decryptMessage)
						.map(Long::parseLong)
						.toList(),
				Optional.ofNullable(request.intermediaryId()).filter(StringUtils::isNotBlank).map(cipher::getDecryptedId).orElse(null),
				cipher.getPrincipalId()
		);
	}
}
