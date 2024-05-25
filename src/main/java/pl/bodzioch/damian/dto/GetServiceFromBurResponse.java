package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.client.bur.BurServiceDto;
import pl.bodzioch.damian.service.ServiceType;

import java.io.Serializable;
import java.time.LocalDate;

public record GetServiceFromBurResponse(
        String number,
        String title,
        ServiceType serviceType,
        LocalDate startDate,
        LocalDate endDate,
        String serviceProviderId
) implements Serializable {

    public GetServiceFromBurResponse(BurServiceDto burServiceDto, String serviceProviderId) {
        this(
                burServiceDto.number(),
                burServiceDto.title(),
                ServiceType.ofBurId(burServiceDto.serviceTypeId()),
                burServiceDto.startDate().toLocalDate(),
                burServiceDto.endDate().toLocalDate(),
                serviceProviderId
        );
    }
}
