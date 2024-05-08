package pl.bodzioch.damian.dto;

import jakarta.validation.constraints.*;

public record UpdateOperatorRequest(
		@NotEmpty(message = "error.client.operator.emptyId")
		String id,
		@NotNull(message = "error.client.operator.nullVersion")
		@Min(value = 0, message = "error.client.operator.minVersion")
		@Max(value = Integer.MAX_VALUE, message = "error.client.operator.maxVersion")
		Integer version,
		@NotEmpty(message = "error.client.serviceProvider.emptyName")
		@Pattern(regexp = "[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9 -/.\"\\\\]{1,150}", message = "error.client.operator.incorrectName")
		String name,
		String notes
) {
}
