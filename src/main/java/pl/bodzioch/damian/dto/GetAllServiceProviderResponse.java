package pl.bodzioch.damian.dto;

import java.io.Serializable;
import java.util.List;

public record GetAllServiceProviderResponse(
        List<ServiceProviderData> serviceProviders
) implements Serializable {
}
