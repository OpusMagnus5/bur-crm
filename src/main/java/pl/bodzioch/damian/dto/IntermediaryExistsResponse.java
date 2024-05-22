package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record IntermediaryExistsResponse(
        Boolean exists
) implements Serializable {
}
