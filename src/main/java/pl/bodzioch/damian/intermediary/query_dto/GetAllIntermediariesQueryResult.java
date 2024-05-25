package pl.bodzioch.damian.intermediary.query_dto;

import pl.bodzioch.damian.infrastructure.query.QueryResult;
import pl.bodzioch.damian.intermediary.IntermediaryDto;

import java.util.List;

public record GetAllIntermediariesQueryResult(
        List<IntermediaryDto> intermediaries
) implements QueryResult {
}
