package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record DeleteOperatorResponse(
        String message
) implements Serializable {
}
