package pl.bodzioch.damian.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.pl.NIP;

public record UpdateIntermediaryRequest(
		@NotEmpty(message = "error.client.intermediary.emptyId")
		String id,
		@NotNull(message = "error.client.intermediary.nullVersion")
		@Min(value = 0, message = "error.client.intermediary.minVersion")
		@Max(value = Integer.MAX_VALUE, message = "error.client.intermediary.maxVersion")
		Integer version,
		@NotEmpty(message = "error.client.intermediary.emptyName")
		@Pattern(regexp = "[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9 -/.\"\\\\]{1,150}", message = "error.client.intermediary.incorrectName")
		String name,
		@NotEmpty(message = "error.client.intermediary.emptyNIP")
		@NIP(message = "error.client.intermediary.incorrectNIP")
		String nip
) {
}
