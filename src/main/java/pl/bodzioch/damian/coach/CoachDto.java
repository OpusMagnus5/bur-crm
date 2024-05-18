package pl.bodzioch.damian.coach;

import pl.bodzioch.damian.user.InnerUserDto;

import java.time.LocalDateTime;

public record CoachDto(
        Long id,
        Integer version,
        String firstName,
        String lastName,
        String pesel,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Long modifiedBy,
        Long createdBy,
        InnerUserDto creator,
        InnerUserDto modifier
) {

    CoachDto(Coach coach) {
        this(
                coach.id(),
                coach.version(),
                coach.firstName(),
                coach.lastName(),
                coach.pesel(),
                coach.createdAt(),
                coach.modifiedAt(),
                coach.modifiedBy(),
                coach.createdBy(),
                new InnerUserDto(coach.creator()),
                new InnerUserDto(coach.modifier())
        );
    }
}
