package pl.bodzioch.damian.service;

import pl.bodzioch.damian.customer.InnerCustomerDto;
import pl.bodzioch.damian.operator.InnerOperatorDto;
import pl.bodzioch.damian.user.InnerUserDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ServiceDto(
        Long id,
        UUID uuid,
        Long burCardId,
        String number,
        String name,
        ServiceType type,
        LocalDate startDate,
        LocalDate endDate,
        Integer numberOfParticipants,
        Long serviceProviderId,
        Long programId,
        Long customerId,
        List<Long> coachIds,
        Long intermediaryId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Long createdBy,
        Long modifiedBy,

        InnerUserDto creator,
        InnerUserDto modifier,
        InnerOperatorDto operator,
        InnerCustomerDto customer
) {

    ServiceDto(Service service) {
        this(
                service.id(),
                service.uuid(),
                service.burCardId(),
                service.number(),
                service.name(),
                service.type(),
                service.startDate(),
                service.endDate(),
                service.numberOfParticipants(),
                service.serviceProviderId(),
                service.programId(),
                service.customerId(),
                service.coachIds(),
                service.intermediaryId(),
                service.createdAt(),
                service.modifiedAt(),
                service.createdBy(),
                service.modifiedBy(),
                new InnerUserDto(service.creator()),
                new InnerUserDto(service.modifier()),
                new InnerOperatorDto(service.operator()),
                new InnerCustomerDto(service.customer())
        );
    }
}
