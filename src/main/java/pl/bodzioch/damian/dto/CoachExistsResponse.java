package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record CoachExistsResponse(
        Boolean exists
) implements Serializable {
}
