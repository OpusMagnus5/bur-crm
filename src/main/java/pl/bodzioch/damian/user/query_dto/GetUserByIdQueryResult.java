package pl.bodzioch.damian.user.query_dto;

import pl.bodzioch.damian.infrastructure.query.QueryResult;
import pl.bodzioch.damian.user.UserDto;

public record GetUserByIdQueryResult(

		UserDto userDto
) implements QueryResult {
}
