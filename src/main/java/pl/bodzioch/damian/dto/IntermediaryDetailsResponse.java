package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.intermediary.IntermediaryDto;
import pl.bodzioch.damian.user.InnerUserDto;
import pl.bodzioch.damian.utils.CipherComponent;

import java.time.LocalDateTime;
import java.util.Optional;

public record IntermediaryDetailsResponse(
        String id,
        Integer version,
        String name,
        String nip,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        String creatorFirstName,
        String creatorLastName,
        String modifierFirstName,
        String modifierLastName
) {

    public IntermediaryDetailsResponse(IntermediaryDto intermediary, CipherComponent cipher) {
        this(
                cipher.encryptMessage(intermediary.id().toString()),
                intermediary.version(),
                intermediary.name(),
                intermediary.nip().toString(),
                intermediary.createdAt(),
                intermediary.modifiedAt(),
                intermediary.creator().firstName(),
                intermediary.creator().lastName(),
                Optional.ofNullable(intermediary.modifier()).map(InnerUserDto::firstName).orElse(null),
                Optional.ofNullable(intermediary.modifier()).map(InnerUserDto::lastName).orElse(null)
        );
    }
}
