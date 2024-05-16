package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.customer.CustomerDto;
import pl.bodzioch.damian.user.InnerUserDto;
import pl.bodzioch.damian.utils.CipherComponent;

import java.time.LocalDateTime;
import java.util.Optional;

public record CustomerDetailsResponse(
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

    public CustomerDetailsResponse(CustomerDto customer, CipherComponent cipher) {
        this(
                cipher.encryptMessage(customer.id().toString()),
                customer.version(),
                customer.name(),
                customer.nip().toString(),
                customer.createdAt(),
                customer.modifiedAt(),
                customer.creator().firstName(),
                customer.creator().lastName(),
                Optional.ofNullable(customer.modifier()).map(InnerUserDto::firstName).orElse(null),
                Optional.ofNullable(customer.modifier()).map(InnerUserDto::lastName).orElse(null)
        );
    }
}
