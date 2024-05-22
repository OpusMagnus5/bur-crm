package pl.bodzioch.damian.intermediary.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetIntermediaryDetailsQuery(
        Long id
) implements Query<GetIntermediaryDetailsQueryResult> {
}
