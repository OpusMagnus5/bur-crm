package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record CreateNewServiceProviderResponse(
        String message
) implements Serializable {
}
