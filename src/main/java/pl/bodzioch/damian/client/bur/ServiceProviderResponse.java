package pl.bodzioch.damian.client.bur;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

record ServiceProviderResponse(

        @JsonProperty("lista")
        List<ServiceProviderBur> serviceProviders
) {

        BurServiceProviderDto mapFirst() {
                return serviceProviders().stream()
                        .map(provider -> new BurServiceProviderDto(provider.id().longValue(), provider.name()))
                        .findFirst()
                        .orElse(null);
        }
}
