package pl.bodzioch.damian.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.pl.NIP;

import java.io.Serializable;

public record CreateNewServiceProviderRequest(

        @NotEmpty(message = "error.client.serviceProvider.createNew.emptyName")
        @Pattern(regexp = "[a-zA-ZążęćłóńĄŻĘĆŁÓŃ -/.\"\\\\]{1,150}", message = "error.client.serviceProvider.createNew.incorrectName")
        String name,
        @NotEmpty(message = "error.client.serviceProvider.createNew.emptyNIP")
        @NIP(message = "error.client.serviceProvider.createNew.incorrectNIP")
        String nip

) implements Serializable {
}
