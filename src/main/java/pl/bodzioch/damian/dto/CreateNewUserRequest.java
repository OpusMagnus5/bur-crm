package pl.bodzioch.damian.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import pl.bodzioch.damian.user.validator.UserRoleV;

public record CreateNewUserRequest(

        @NotEmpty(message = "error.client.emailEmpty")
        @Email(message = "error.client.incorrectEmail")
        String email,
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
