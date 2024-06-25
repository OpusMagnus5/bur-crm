package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record ChangeUserPasswordResponse(
        String message
) implements Serializable {
}
