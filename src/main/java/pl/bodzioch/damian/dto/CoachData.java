package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.coach.CoachDto;
import pl.bodzioch.damian.utils.CipherComponent;

public record CoachData(
        String id,
        String firstName,
        String lastName,
        String pesel
) {

    public CoachData(CoachDto coach, CipherComponent cipher) {
        this(
                cipher.encryptMessage(coach.id().toString()),
                coach.firstName(),
                coach.lastName(),
                coach.pesel()
        );
    }
}
