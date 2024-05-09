package pl.bodzioch.damian.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

public record CreateNewProgramRequest(
        @NotEmpty(message = "error.client.program.emptyOperatorId")
        String operatorId,
        @NotEmpty(message = "error.client.program.emptyName")
        @Pattern(regexp = "[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9 -/.\"\\\\]{1,150}", message = "error.client.program.incorrectName")
        String name
) implements Serializable {
}
