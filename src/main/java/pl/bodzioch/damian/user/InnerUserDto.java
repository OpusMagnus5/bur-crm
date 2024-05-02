package pl.bodzioch.damian.user;

public record InnerUserDto(

        String firstName,
        String lastName
) {

    public InnerUserDto(InnerUser user) {
        this(
                user.firstName(),
                user.lastName()
        );
    }
}
