package pl.bodzioch.damian.dto;

import java.util.List;

public record ServiceProviderPageResponse(
        List<ServiceProviderData> providers,
        Long totalProviders
) {
}
