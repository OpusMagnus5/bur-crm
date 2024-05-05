package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record OperatorExistsResponse(
        boolean exists
) implements Serializable {
}
