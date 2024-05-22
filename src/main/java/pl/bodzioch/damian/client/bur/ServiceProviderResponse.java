package pl.bodzioch.damian.client.bur;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.bodzioch.damian.exception.HttpClientException;

import java.util.List;

record ServiceProviderResponse(

        @JsonProperty("lista")
        List<ServiceProviderBur> serviceProviders
) {

        BurServiceProviderDto mapFirst() {
                return serviceProviders().stream()
                        .map(provider -> new BurServiceProviderDto(provider.id(), provider.name()))
                        .findFirst()
                        .orElseThrow(() -> new HttpClientException("Empty service providers in response"));
        }
}
