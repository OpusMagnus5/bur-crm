package pl.bodzioch.damian.program.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;
import pl.bodzioch.damian.operator.OperatorFilterField;

import java.util.Map;

public record GetProgramPageQuery(
        int pageNumber,
        int pageSize,
        Map<OperatorFilterField, ?> filters
) implements Query<GetProgramPageQueryResult> {
}
