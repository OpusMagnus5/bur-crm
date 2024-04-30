package pl.bodzioch.damian.client.bur;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

record ServiceProviderResponse(

        @JsonProperty("lista")
        List<ServiceProviderBur> serviceProviders
) {
}
