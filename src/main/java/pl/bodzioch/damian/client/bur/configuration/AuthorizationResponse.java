package pl.bodzioch.damian.client.bur.configuration;

import java.io.Serializable;

record AuthorizationResponse(

        String token
) implements Serializable {
}
