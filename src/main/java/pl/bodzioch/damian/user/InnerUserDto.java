package pl.bodzioch.damian.user;

import java.util.Optional;

public record InnerUserDto(

        String firstName,
        String lastName
) {

    public InnerUserDto(InnerUser user) {
        this(
                Optional.ofNullable(user).map(InnerUser::firstName).orElse(null),
                Optional.ofNullable(user).map(InnerUser::lastName).orElse(null)
        );
    }
}
