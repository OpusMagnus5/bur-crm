package pl.bodzioch.damian.client.bur.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

record AuthorizationRequest(

        @JsonProperty("nazwaUzytkownika")
        String username,
        @JsonProperty("kluczAutoryzacyjny")
        String key

) implements Serializable {
}
