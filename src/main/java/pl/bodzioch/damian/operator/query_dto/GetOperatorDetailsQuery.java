package pl.bodzioch.damian.operator.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetOperatorDetailsQuery(
		Long id
) implements Query<GetOperatorDetailsQueryResult> {
}
