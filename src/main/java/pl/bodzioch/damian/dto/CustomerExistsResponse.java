package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record CustomerExistsResponse(
        Boolean exists
) implements Serializable {
}
