package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.service.ServiceDto;
import pl.bodzioch.damian.utils.CipherComponent;
import pl.bodzioch.damian.utils.MessageResolver;

import java.io.Serializable;
import java.time.LocalDate;

public record ServiceData(
        String id,
        String number,
        String name,
        String type,
        LocalDate startDate,
        LocalDate endDate,
        OperatorData operator,
        CustomerData customer,
        ServiceProviderData serviceProvider
) implements Serializable {

    public ServiceData(ServiceDto serviceDto, CipherComponent cipher, MessageResolver messageResolver) {
        this(
                cipher.encryptMessage(serviceDto.id().toString()),
                serviceDto.number(),
                serviceDto.name(),
                messageResolver.getMessage("service.type." + serviceDto.type().name()),
                serviceDto.startDate(),
                serviceDto.endDate(),
                new OperatorData(serviceDto.operator(), cipher),
                new CustomerData(serviceDto.customer(), cipher),
                new ServiceProviderData(serviceDto.serviceProvider(), cipher)
        );
    }
}
