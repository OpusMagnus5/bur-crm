package pl.bodzioch.damian.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.pl.NIP;

import java.io.Serializable;

public record CreateNewIntermediaryRequest(
        @NotEmpty(message = "error.client.intermediary.emptyName")
        @Pattern(regexp = "[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9 -/.\"\\\\]{1,150}", message = "error.client.intermediary.incorrectName")
        String name,
        @NotEmpty(message = "error.client.intermediary.emptyNIP")
        @NIP(message = "error.client.intermediary.incorrectNIP")
        String nip
) implements Serializable {
}
