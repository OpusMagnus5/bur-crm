package pl.bodzioch.damian.service_provider.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetServiceProviderDetailsQuery(

        Long id

) implements Query<GetServiceProviderDetailsQueryResult> {
}
