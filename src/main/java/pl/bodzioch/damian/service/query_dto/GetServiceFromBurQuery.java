package pl.bodzioch.damian.service.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetServiceFromBurQuery(
        String serviceNumber
) implements Query<GetServiceFromBurQueryResult> {
}
