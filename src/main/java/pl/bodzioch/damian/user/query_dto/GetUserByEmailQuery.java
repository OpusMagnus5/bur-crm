package pl.bodzioch.damian.user.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetUserByEmailQuery(

        String email

) implements Query<GetUserByEmailQueryResult> {
}
