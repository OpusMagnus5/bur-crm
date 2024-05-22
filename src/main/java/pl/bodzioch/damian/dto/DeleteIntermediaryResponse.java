package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record DeleteIntermediaryResponse(
        String message
) implements Serializable {
}
