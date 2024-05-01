package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record ProviderNameResponse(
        String name
) implements Serializable {
}
