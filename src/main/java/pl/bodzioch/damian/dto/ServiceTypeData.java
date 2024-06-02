package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.service.ServiceType;
import pl.bodzioch.damian.utils.MessageResolver;

import java.util.Optional;

public record ServiceTypeData(
        String value,
        String name
) {
    public ServiceTypeData(ServiceType serviceType, MessageResolver messageResolver) {
        this(
                Optional.ofNullable(serviceType).map(Enum::name).orElse(null),
                Optional.ofNullable(serviceType).map(Enum::name)
                        .map(name -> messageResolver.getMessage("service.type." + name))
                        .orElse(null)
        );
    }
}
