package pl.bodzioch.damian.operator.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetOperatorsPageQuery(
        int pageNumber,
        int pageSize
) implements Query<GetOperatorsPageQueryResult> {
}
