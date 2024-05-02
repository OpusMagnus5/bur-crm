package pl.bodzioch.damian.service_provider;

import java.time.LocalDateTime;
import java.util.UUID;

public record ServiceProviderDto(

        Long id,
        UUID uuid,
        Integer version,
        Long burId,
        String name,
        Long nip,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    ServiceProviderDto(ServiceProvider serviceProvider) {
        this(
                serviceProvider.spr_id(),
                serviceProvider.spr_uuid(),
                serviceProvider.spr_version(),
                serviceProvider.spr_bur_id(),
                serviceProvider.spr_name(),
                serviceProvider.spr_nip(),
                serviceProvider.spr_created_at(),
                serviceProvider.spr_modified_at()
        );
    }
}
