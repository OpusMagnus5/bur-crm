package pl.bodzioch.damian.intermediary.query_dto;

import pl.bodzioch.damian.infrastructure.query.QueryResult;
import pl.bodzioch.damian.intermediary.IntermediaryDto;

public record GetIntermediaryByNipQueryResult(
        IntermediaryDto intermediary
) implements QueryResult {
}
