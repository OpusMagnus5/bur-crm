package pl.bodzioch.damian.service;

import pl.bodzioch.damian.coach.InnerCoachDto;
import pl.bodzioch.damian.customer.InnerCustomerDto;
import pl.bodzioch.damian.document.InnerDocumentDto;
import pl.bodzioch.damian.intermediary.InnerIntermediaryDto;
import pl.bodzioch.damian.operator.InnerOperatorDto;
import pl.bodzioch.damian.program.InnerProgramDto;
import pl.bodzioch.damian.service_provider.InnerServiceProviderDto;
import pl.bodzioch.damian.user.InnerUserDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ServiceDto(
        Long id,
        UUID uuid,
        Integer version,
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
        ServiceStatus status,

        InnerUserDto creator,
        InnerUserDto modifier,
        InnerOperatorDto operator,
        InnerCustomerDto customer,
        InnerServiceProviderDto serviceProvider,
        InnerProgramDto program,
        InnerIntermediaryDto intermediary,
        List<InnerCoachDto> coaches,
        List<InnerDocumentDto> documents,
        List<BadgeMessage> badgeMessages
) {

    ServiceDto(Service service) {
        this(
                service.id(),
                service.uuid(),
                service.version(),
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
                service.status(),
                new InnerUserDto(service.creator()),
                new InnerUserDto(service.modifier()),
                new InnerOperatorDto(service.operator()),
                new InnerCustomerDto(service.customer()),
                new InnerServiceProviderDto(service.serviceProvider()),
                new InnerProgramDto(service.program()),
                new InnerIntermediaryDto(service.intermediary()),
                service.coaches().stream()
                        .map(InnerCoachDto::new)
                        .toList(),
                service.documents().stream()
                        .map(InnerDocumentDto::new)
                        .toList(),
                service.getBadgeMessages()
        );
    }
}
