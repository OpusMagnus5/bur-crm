package pl.bodzioch.damian.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.pl.NIP;

import java.io.Serializable;

public record CreateNewCustomerRequest(
        @NotEmpty(message = "error.client.customer.emptyName")
        @Pattern(regexp = "[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9 -/.\"\\\\]{1,150}", message = "error.client.customer.incorrectName")
        String name,
        @NotEmpty(message = "error.client.customer.emptyNIP")
        @NIP(message = "error.client.customer.incorrectNIP")
        String nip
) implements Serializable {
}
