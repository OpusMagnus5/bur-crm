package pl.bodzioch.damian.user.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;
import pl.bodzioch.damian.user.UserFilterField;

import java.util.HashMap;

public record GetUsersPageQuery(

        HashMap<UserFilterField, Object> filters,
        int pageNumber,
        int pageSize

) implements Query<GetUsersPageQueryResult> {
}
