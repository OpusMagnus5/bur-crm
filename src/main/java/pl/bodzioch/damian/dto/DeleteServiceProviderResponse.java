package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record DeleteServiceProviderResponse(
        String message
) implements Serializable {
}
