package pl.bodzioch.damian.client.bur;

import com.fasterxml.jackson.annotation.JsonProperty;

record ServiceProviderBur(
        Integer id,
        @JsonProperty("nazwa")
        String name
) {

}
