package pl.bodzioch.damian.user;

import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbConstructor;
import pl.bodzioch.damian.infrastructure.database.DbId;

public record InnerUser(
        @DbId
        @DbColumn(name = "usr_id")
        Long id,
        @DbColumn(name = "usr_first_name")
        String firstName,
        @DbColumn(name = "usr_last_name")
        String lastName
) {

    @DbConstructor
    public InnerUser {
    }
}
