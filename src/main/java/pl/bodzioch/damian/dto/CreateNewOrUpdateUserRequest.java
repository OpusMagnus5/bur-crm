package pl.bodzioch.damian.dto;

import jakarta.validation.constraints.*;
import pl.bodzioch.damian.user.validator.UserRoleV;

public record CreateNewOrUpdateUserRequest(
        String id,
        @NotEmpty(message = "error.client.emailEmpty")
        @Email(message = "error.client.incorrectEmail")
        String email,
        @Min(value = 0, message = "error.client.user.minVersion")
        @Max(value = Integer.MAX_VALUE, message = "error.client.user.maxVersion")
        Integer version,
        @NotEmpty(message = "error.client.firstNameEmpty")
        @Pattern(regexp = "[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ]{1,15}", message = "error.client.incorrectFirstName")
        String firstName,

        @NotEmpty(message = "error.client.lastNameEmpty")
        @Pattern(regexp = "[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ -]{1,60}", message = "error.client.incorrectLastName")
        String lastName,

        @NotEmpty(message = "error.client.roleEmpty")
        @UserRoleV(message = "error.client.incorrectRole")
        String role
) {
}
