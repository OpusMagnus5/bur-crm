package pl.bodzioch.damian.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record ResetUserPasswordRequest(
        @NotEmpty(message = "error.client.user.emptyId")
        String id,
        @NotNull(message = "error.client.user.nullVersion")
        @Min(value = 0, message = "error.client.user.minVersion")
        @Max(value = Integer.MAX_VALUE, message = "error.client.user.maxVersion")
        Integer userVersion
) implements Serializable {
}
