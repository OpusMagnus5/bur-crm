package pl.bodzioch.damian.intermediary;

import pl.bodzioch.damian.user.InnerUserDto;

import java.time.LocalDateTime;

public record IntermediaryDto(
        Long id,
        Integer version,
        String name,
        Long nip,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Long modifiedBy,
        Long createdBy,
        InnerUserDto creator,
        InnerUserDto modifier
) {

    IntermediaryDto(Intermediary intermediary) {
        this(
                intermediary.id(),
                intermediary.version(),
                intermediary.name(),
                intermediary.nip(),
                intermediary.createdAt(),
                intermediary.modifiedAt(),
                intermediary.modifiedBy(),
                intermediary.createdBy(),
                new InnerUserDto(intermediary.creator()),
                new InnerUserDto(intermediary.modifier())
        );
    }
}
