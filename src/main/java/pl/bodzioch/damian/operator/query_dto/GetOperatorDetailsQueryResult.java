package pl.bodzioch.damian.operator.query_dto;

import pl.bodzioch.damian.infrastructure.query.QueryResult;
import pl.bodzioch.damian.operator.OperatorDto;

public record GetOperatorDetailsQueryResult(
		OperatorDto operator
) implements QueryResult {
}
