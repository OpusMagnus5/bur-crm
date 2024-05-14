package pl.bodzioch.damian.dto;

import jakarta.validation.constraints.*;

import java.io.Serializable;

public record UpdateProgramRequest(
		@NotEmpty(message = "error.client.program.emptyId")
		String id,
		@NotNull(message = "error.client.program.nullVersion")
		@Min(value = 0, message = "error.client.program.minVersion")
		@Max(value = Integer.MAX_VALUE, message = "error.client.program.maxVersion")
		Integer version,
		@NotEmpty(message = "error.client.program.emptyName")
		@Pattern(regexp = "[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9 -/.\"\\\\]{1,150}", message = "error.client.program.incorrectName")
		String name,
		@NotEmpty(message = "error.client.program.emptyOperatorId")
		String operatorId
) implements Serializable {
}
