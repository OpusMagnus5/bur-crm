package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record DeleteProgramResponse(
        String message
) implements Serializable {
}
