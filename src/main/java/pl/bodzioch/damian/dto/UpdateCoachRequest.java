package pl.bodzioch.damian.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.pl.PESEL;

import java.io.Serializable;

public record UpdateCoachRequest(
        @NotEmpty(message = "error.client.coach.emptyId")
        String id,
        @NotNull(message = "error.client.coach.nullVersion")
        @Min(value = 0, message = "error.client.coach.minVersion")
        @Max(value = Integer.MAX_VALUE, message = "error.client.coach.maxVersion")
        Integer version,
        @NotEmpty(message = "error.client.coach.firstNameEmpty")
        @Pattern(regexp = "[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ]{1,15}", message = "error.client.coach.incorrectFirstName")
        String firstName,
        @NotEmpty(message = "error.client.coach.lastNameEmpty")
        @Pattern(regexp = "[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ -]{1,60}", message = "error.client.coach.incorrectLastName")
        String lastName,
        @NotEmpty(message = "error.client.coach.emptyPesel")
        @PESEL(message = "error.client.coach.incorrectPesel")
        String pesel
) implements Serializable {
}
