package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record CreateNewOperatorResponse(
        String message
) implements Serializable {
}
