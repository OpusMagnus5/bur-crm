package pl.bodzioch.damian.client.bur.dto;

import java.io.Serializable;

public record AuthorizationResponse(

        String token
) implements Serializable {
}
