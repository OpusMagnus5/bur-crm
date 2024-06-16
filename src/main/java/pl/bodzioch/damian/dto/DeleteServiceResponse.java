package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record DeleteServiceResponse(
        String message
) implements Serializable {
}
