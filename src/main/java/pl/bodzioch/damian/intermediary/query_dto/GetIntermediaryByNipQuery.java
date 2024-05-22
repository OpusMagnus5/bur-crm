package pl.bodzioch.damian.intermediary.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetIntermediaryByNipQuery(
        Long nip
) implements Query<GetIntermediaryByNipQueryResult> {
}
