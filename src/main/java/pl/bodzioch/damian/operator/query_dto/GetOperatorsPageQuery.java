package pl.bodzioch.damian.operator.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;
import pl.bodzioch.damian.operator.OperatorFilterField;

import java.util.Map;

public record GetOperatorsPageQuery(
        int pageNumber,
        int pageSize,
        Map<OperatorFilterField, ?> filters
) implements Query<GetOperatorsPageQueryResult> {
}
