package pl.bodzioch.damian.client.bur;

public record BurServiceProviderDto(
        Long id,
        String name
) {

	BurServiceProviderDto(ServiceProviderBur serviceProvider) {
		this(
				serviceProvider.id(),
				serviceProvider.name()
		);
	}
}
