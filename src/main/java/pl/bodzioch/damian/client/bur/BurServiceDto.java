package pl.bodzioch.damian.client.bur;

import java.time.LocalDateTime;

public record BurServiceDto(
		Long id,
		String number,
		String title,
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
				service.serviceTypeId(),
				service.startDate(),
				service.endDate(),
				new BurServiceProviderDto(service.serviceProvider())
		);
	}
}
