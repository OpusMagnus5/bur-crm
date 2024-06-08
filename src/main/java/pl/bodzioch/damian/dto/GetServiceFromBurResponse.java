package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.client.bur.BurServiceDto;
import pl.bodzioch.damian.service.ServiceStatus;
import pl.bodzioch.damian.service.ServiceType;
import pl.bodzioch.damian.utils.MessageResolver;

import java.io.Serializable;
import java.time.LocalDate;

public record GetServiceFromBurResponse(
        String number,
        String title,
        ServiceType serviceType,
        LocalDate startDate,
        LocalDate endDate,
        String serviceProviderName,
        ServiceStatusData status
) implements Serializable {

    public GetServiceFromBurResponse(BurServiceDto burServiceDto, String serviceProviderName, MessageResolver messageResolver) {
        this(
                burServiceDto.number(),
                burServiceDto.title(),
                ServiceType.ofBurId(burServiceDto.serviceTypeId()),
                burServiceDto.startDate().toLocalDate(),
                burServiceDto.endDate().toLocalDate(),
                serviceProviderName,
                new ServiceStatusData(ServiceStatus.of(burServiceDto.status()).name(),
                        messageResolver.getMessage("service.status." + ServiceStatus.of(burServiceDto.status()).name()))
        );
    }
}
