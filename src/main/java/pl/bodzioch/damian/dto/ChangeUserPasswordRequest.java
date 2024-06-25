package pl.bodzioch.damian.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import pl.bodzioch.damian.user.validator.Password;

import java.io.Serializable;

public record ChangeUserPasswordRequest(
        @NotEmpty(message = "error.client.user.emptyId")
        String userId,
        @NotNull(message = "error.client.user.nullVersion")
        @Min(value = 0, message = "error.client.user.minVersion")
        @Max(value = Integer.MAX_VALUE, message = "error.client.user.maxVersion")
        Integer version,
        @NotEmpty(message = "error.client.user.emptyPassword")
        @Password(message = "error.client.user.incorrectPassword")
        String password
) implements Serializable {
}
