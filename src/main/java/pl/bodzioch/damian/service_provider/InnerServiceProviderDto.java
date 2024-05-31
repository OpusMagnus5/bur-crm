package pl.bodzioch.damian.service_provider;

import java.util.Optional;

public record InnerServiceProviderDto(
        Long id,
        String name
) {
    public InnerServiceProviderDto(InnerServiceProvider serviceProvider) {
        this(
                Optional.ofNullable(serviceProvider).map(InnerServiceProvider::id).orElse(null),
                Optional.ofNullable(serviceProvider).map(InnerServiceProvider::name).orElse(null)
        );
    }
}
