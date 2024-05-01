package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record ProviderExistsResponse(
        boolean exists
) implements Serializable {
}
