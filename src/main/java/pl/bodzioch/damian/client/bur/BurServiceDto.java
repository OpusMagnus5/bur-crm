package pl.bodzioch.damian.client.bur;

import java.time.LocalDateTime;

public record BurServiceDto(
		Long id,
		String number,
		String title,
		BurServiceStatus status,
		Long serviceTypeId,
		LocalDateTime startDate,
		LocalDateTime endDate,
		BurServiceProviderDto serviceProvider
) {
	BurServiceDto(ServiceBur service) {
		this(
				service.id(),
				service.number(),
				service.title(),
				service.status(),
				service.serviceTypeId(),
				service.startDate().toLocalDateTime(),
				service.endDate().toLocalDateTime(),
				new BurServiceProviderDto(service.serviceProvider())
		);
	}
}
