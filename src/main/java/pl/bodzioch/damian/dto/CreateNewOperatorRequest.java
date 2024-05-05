package pl.bodzioch.damian.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

public record CreateNewOperatorRequest(

        @NotEmpty(message = "error.client.operator.emptyName")
        @Pattern(regexp = "[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9 -/.\"\\\\]{1,150}", message = "error.client.operator.incorrectName")
        String name,
        String notes
) implements Serializable {
}
