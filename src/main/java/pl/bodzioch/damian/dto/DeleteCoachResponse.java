package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record DeleteCoachResponse(
        String message
) implements Serializable {
}
