package pl.bodzioch.damian.customer;

import pl.bodzioch.damian.user.InnerUserDto;

import java.time.LocalDateTime;

public record CustomerDto(
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

    CustomerDto(Customer customer) {
        this(
                customer.id(),
                customer.version(),
                customer.name(),
                customer.nip(),
                customer.createdAt(),
                customer.modifiedAt(),
                customer.modifiedBy(),
                customer.createdBy(),
                new InnerUserDto(customer.creator()),
                new InnerUserDto(customer.modifier())
        );
    }
}
