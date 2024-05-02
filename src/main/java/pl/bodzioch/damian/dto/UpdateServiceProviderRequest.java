package pl.bodzioch.damian.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.pl.NIP;

public record UpdateServiceProviderRequest(
        @NotEmpty(message = "error.client.serviceProvider.emptyId")
        String id,
        @NotNull(message = "error.client.serviceProvider.nullVersion")
        @Min(value = 0, message = "error.client.serviceProvider.minVersion")
        @Max(value = Integer.MAX_VALUE, message = "error.client.serviceProvider.maxVersion")
        Integer version,
        @NotEmpty(message = "error.client.serviceProvider.emptyName")
        @Pattern(regexp = "[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ -/.\"\\\\]{1,150}", message = "error.client.serviceProvider.incorrectName")
        String name,
        @NotEmpty(message = "error.client.serviceProvider.emptyNIP")
        @NIP(message = "error.client.serviceProvider.incorrectNIP")
        String nip
) {
}
