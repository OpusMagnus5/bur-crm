package pl.bodzioch.damian.client.bur;

import com.fasterxml.jackson.annotation.JsonProperty;

record ServiceProviderBur(
        Long id,
        @JsonProperty("nazwa")
        String name
) {

}
