package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.service.ServiceStatus;
import pl.bodzioch.damian.utils.MessageResolver;

public record ServiceStatusData(
        String value,
        String name
) {
    public ServiceStatusData(ServiceStatus serviceStatus, MessageResolver messageResolver) {
        this(
                serviceStatus.name(),
                messageResolver.getMessage("service.status." + serviceStatus.name())
        );
    }
}
