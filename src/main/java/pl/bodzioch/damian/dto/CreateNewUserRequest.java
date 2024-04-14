package pl.bodzioch.damian.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record CreateNewUserRequest(

        @NotEmpty(message = "error.client.emailEmpty")
        @Email(message = "error.client.incorrectEmail")
        String email,
        @NotEmpty(message = "error.client.firstNameEmpty")
        @Pattern(regexp = "[a-zA-ZążęćłóńĄŻĘĆŁÓŃ]+", message = "error.client.incorrectFirstName")
        String firstName,

        @NotEmpty(message = "error.client.lastNameEmpty")
        @Pattern(regexp = "[a-zA-ZążęćłóńĄŻĘĆŁÓŃ -]+", message = "error.client.incorrectLastName")
        String lastName
) {
}
