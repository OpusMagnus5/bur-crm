package pl.bodzioch.damian.coach;

import java.util.Optional;

public record InnerCoachDto(
        Long id,
        String firstName,
        String lastName
) {

    public InnerCoachDto(InnerCoach coach) {
        this(
                Optional.ofNullable(coach).map(InnerCoach::id).orElse(null),
                Optional.ofNullable(coach).map(InnerCoach::firstName).orElse(null),
                Optional.ofNullable(coach).map(InnerCoach::lastName).orElse(null)
        );
    }
}
