package pl.bodzioch.damian.user;

import pl.bodzioch.damian.infrastructure.query.QueryResult;

public record GetUserByEmailQueryResult(

        UserDto userDto

) implements QueryResult {
}
