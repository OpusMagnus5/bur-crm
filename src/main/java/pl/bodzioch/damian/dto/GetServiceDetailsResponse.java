package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.service.ServiceDto;
import pl.bodzioch.damian.utils.CipherComponent;
import pl.bodzioch.damian.utils.MessageResolver;

import java.time.LocalDate;
import java.util.List;

public record GetServiceDetailsResponse(
        String id,
        Integer version,
        String number,
        String name,
        ServiceTypeData type,
        LocalDate startDate,
        LocalDate endDate,
        Integer numberOfParticipants,
        UserListData creator,
        UserListData modifier,
        OperatorData operator,
        CustomerData customer,
        ServiceProviderData serviceProvider,
        ProgramData program,
        IntermediaryData intermediary,
        List<CoachData> coaches
) {
    public GetServiceDetailsResponse(ServiceDto service, MessageResolver messageResolver, CipherComponent cipher) {
        this(
                cipher.encryptMessage(service.id().toString()),
                service.version(),
                service.number(),
                service.name(),
                new ServiceTypeData(service.type(), messageResolver),
                service.startDate(),
                service.endDate(),
                service.numberOfParticipants(),
                new UserListData(service.creator(), cipher),
                new UserListData(service.modifier(), cipher),
                new OperatorData(service.operator(), cipher),
                new CustomerData(service.customer(), cipher),
                new ServiceProviderData(service.serviceProvider(), cipher),
                new ProgramData(service.program(), cipher),
                new IntermediaryData(service.intermediary(), cipher),
                service.coaches().stream()
                        .map(item -> new CoachData(item, cipher))
                        .toList()
        );
    }
}
