package pl.bodzioch.damian.user.queryDto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetUserByEmailQuery(

        String email

) implements Query<GetUserByEmailQueryResult> {
}
