package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.service.ServiceDto;
import pl.bodzioch.damian.utils.CipherComponent;

import java.io.Serializable;
import java.time.LocalDate;

public record ServiceData(
        String id,
        String number,
        String name,
        String type,
        LocalDate startDate,
        LocalDate endDate,
        String serviceProviderId,
        String programId,
        String customerId,
        OperatorData operator,
        CustomerData customer,
        ServiceProviderData serviceProvider
) implements Serializable {

    public ServiceData(ServiceDto serviceDto, CipherComponent cipher) {
        this(
                cipher.encryptMessage(serviceDto.id().toString()),
                serviceDto.number(),
                serviceDto.name(),
                serviceDto.type().name(),
                serviceDto.startDate(),
                serviceDto.endDate(),
                cipher.encryptMessage(serviceDto.serviceProviderId().toString()),
                cipher.encryptMessage(serviceDto.programId().toString()),
                cipher.encryptMessage(serviceDto.customerId().toString()),
                new OperatorData(serviceDto.operator(), cipher),
                new CustomerData(serviceDto.customer(), cipher),
                new ServiceProviderData(serviceDto.serviceProvider(), cipher)
        );
    }
}
