package pl.bodzioch.damian.user.query_dto;

import pl.bodzioch.damian.infrastructure.query.QueryResult;
import pl.bodzioch.damian.user.UserDto;

import java.util.List;

public record GetUsersPageQueryResult(

        List<UserDto> users,
        long totalUsers

) implements QueryResult {
}
