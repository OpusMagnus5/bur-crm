package pl.bodzioch.damian.operator.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetAllOperatorsQuery() implements Query<GetAllOperatorsQueryResult> {
}
