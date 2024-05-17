package pl.bodzioch.damian.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.pl.NIP;

public record UpdateCustomerRequest(
		@NotEmpty(message = "error.client.customer.emptyId")
		String id,
		@NotNull(message = "error.client.customer.nullVersion")
		@Min(value = 0, message = "error.client.customer.minVersion")
		@Max(value = Integer.MAX_VALUE, message = "error.client.customer.maxVersion")
		Integer version,
		@NotEmpty(message = "error.client.customer.emptyName")
		@Pattern(regexp = "[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9 -/.\"\\\\]{1,150}", message = "error.client.customer.incorrectName")
		String name,
		@NotEmpty(message = "error.client.customer.emptyNIP")
		@NIP(message = "error.client.customer.incorrectNIP")
		String nip
) {
}
