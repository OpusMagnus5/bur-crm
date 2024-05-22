package pl.bodzioch.damian.intermediary.query_dto;

import pl.bodzioch.damian.infrastructure.query.QueryResult;
import pl.bodzioch.damian.intermediary.IntermediaryDto;

import java.util.List;

public record GetIntermediaryPageQueryResult(
        List<IntermediaryDto> intermediaries,
        long totalIntermediaries
) implements QueryResult {
}
