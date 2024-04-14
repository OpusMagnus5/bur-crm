package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record CreateUserResponse(

        String login,
        String password,
        String message

) implements Serializable {
}
