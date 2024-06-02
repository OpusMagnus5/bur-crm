package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.coach.CoachDto;
import pl.bodzioch.damian.coach.InnerCoachDto;
import pl.bodzioch.damian.utils.CipherComponent;

import java.util.Optional;

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

    public CoachData(InnerCoachDto coach, CipherComponent cipher) {
        this(
                Optional.ofNullable(coach).map(InnerCoachDto::id)
                                .map(item -> cipher.encryptMessage(item.toString())).orElse(null),
                Optional.ofNullable(coach).map(InnerCoachDto::firstName).orElse(null),
                Optional.ofNullable(coach).map(InnerCoachDto::lastName).orElse(null),
                null
        );
    }
}
