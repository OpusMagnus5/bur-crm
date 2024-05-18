package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record CreateNewCoachResponse(
        String message
) implements Serializable {
}
