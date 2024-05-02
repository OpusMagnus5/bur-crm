package pl.bodzioch.damian.service_provider.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetProvidersPageQuery(
        int pageNumber,
        int pageSize
) implements Query<GetProvidersPageQueryResult> {
}
