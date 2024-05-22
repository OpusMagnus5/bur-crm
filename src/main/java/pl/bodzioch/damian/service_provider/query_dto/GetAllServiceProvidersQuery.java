package pl.bodzioch.damian.service_provider.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetAllServiceProvidersQuery() implements Query<GetAllServiceProvidersQueryResult> {
}
