package pl.bodzioch.damian.service.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetServiceDetailsQuery(
        Long id
) implements Query<GetServiceDetailsQueryResult> {
}
