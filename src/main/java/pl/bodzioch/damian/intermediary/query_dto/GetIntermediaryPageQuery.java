package pl.bodzioch.damian.intermediary.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;
import pl.bodzioch.damian.intermediary.IntermediaryFilterField;

import java.util.Map;

public record GetIntermediaryPageQuery(
        int pageNumber,
        int pageSize,
        Map<IntermediaryFilterField, ?> filters
) implements Query<GetIntermediaryPageQueryResult> {
}
