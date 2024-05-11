package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record ProgramExistsResponse(
        Boolean exists
) implements Serializable {
}
