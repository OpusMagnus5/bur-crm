package pl.bodzioch.damian.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.pl.PESEL;

public record CreateNewCoachRequest(
        @NotEmpty(message = "error.client.coach.firstNameEmpty")
        @Pattern(regexp = "[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ]{1,15}", message = "error.client.coach.incorrectFirstName")
        String firstName,
        @NotEmpty(message = "error.client.coach.lastNameEmpty")
        @Pattern(regexp = "[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ -]{1,60}", message = "error.client.coach.incorrectLastName")
        String lastName,
        @NotEmpty(message = "error.client.coach.emptyPesel")
        @PESEL(message = "error.client.coach.incorrectPesel")
        String pesel
) {
}
