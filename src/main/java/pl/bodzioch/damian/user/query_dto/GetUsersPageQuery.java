package pl.bodzioch.damian.user.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetUsersPageQuery(

        int pageNumber,
        int pageSize

) implements Query<GetUsersPageQueryResult> {
}
