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
}
