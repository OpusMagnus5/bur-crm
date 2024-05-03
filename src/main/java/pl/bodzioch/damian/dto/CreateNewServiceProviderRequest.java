package pl.bodzioch.damian.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.pl.NIP;

import java.io.Serializable;

public record CreateNewServiceProviderRequest(

        @NotEmpty(message = "error.client.serviceProvider.emptyName")
        @Pattern(regexp = "[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9 -/.\"\\\\]{1,150}", message = "error.client.serviceProvider.incorrectName")
        String name,
        @NotEmpty(message = "error.client.serviceProvider.emptyNIP")
        @NIP(message = "error.client.serviceProvider.incorrectNIP")
        String nip

) implements Serializable {
}
