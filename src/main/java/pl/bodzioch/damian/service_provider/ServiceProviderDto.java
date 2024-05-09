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
                serviceProvider.id(),
                serviceProvider.uuid(),
                serviceProvider.version(),
                serviceProvider.burId(),
                serviceProvider.name(),
                serviceProvider.nip(),
                serviceProvider.createdAt(),
                serviceProvider.modifiedAt(),
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
