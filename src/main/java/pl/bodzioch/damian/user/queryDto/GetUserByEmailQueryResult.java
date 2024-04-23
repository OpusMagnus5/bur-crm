package pl.bodzioch.damian.user.queryDto;

import pl.bodzioch.damian.infrastructure.query.QueryResult;
import pl.bodzioch.damian.user.UserDto;

public record GetUserByEmailQueryResult(

        UserDto userDto

) implements QueryResult {
}
