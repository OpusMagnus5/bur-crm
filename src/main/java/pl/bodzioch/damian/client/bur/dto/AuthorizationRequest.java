package pl.bodzioch.damian.client.bur.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.bodzioch.damian.client.bur.BurCredentialsProperties;

import java.io.Serializable;

public record AuthorizationRequest(

        @JsonProperty("nazwaUzytkownika")
        String username,
        @JsonProperty("kluczAutoryzacyjny")
        String key

) implements Serializable {

    public AuthorizationRequest(BurCredentialsProperties properties) {
        this(properties.username(), properties.key());
    }
}
