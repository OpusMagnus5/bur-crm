package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record DeleteUserByIdResponse(
        String message
) implements Serializable {
}
