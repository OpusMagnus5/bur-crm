package pl.bodzioch.damian.service_provider;

import pl.bodzioch.damian.user.InnerUserDto;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public record ServiceProviderDto(

        Long id,
        UUID uuid,
        Integer version,
        Long burId,
        String name,
        Long nip,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        InnerUserDto creator,
        InnerUserDto modifier
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
                serviceProvider.spr_modified_at(),
                new InnerUserDto(serviceProvider.creator()),
                new InnerUserDto(serviceProvider.modifier())
        );
    }

    public Optional<InnerUserDto> getCreator() {
        return Optional.ofNullable(creator);
    }

    public Optional<InnerUserDto>  getModifier() {
        return Optional.ofNullable(modifier);
    }
}
