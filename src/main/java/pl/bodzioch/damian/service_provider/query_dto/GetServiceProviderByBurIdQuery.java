package pl.bodzioch.damian.service_provider.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetServiceProviderByBurIdQuery(
        Long burId
) implements Query<GetServiceProviderByBurIdQueryResult> {
}
