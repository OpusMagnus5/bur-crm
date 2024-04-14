package pl.bodzioch.damian.user;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetUserByEmailQuery(

        String email

) implements Query<GetUserByEmailQueryResult> {
}
