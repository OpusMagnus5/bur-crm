package pl.bodzioch.damian.service_provider.query_dto;

import pl.bodzioch.damian.infrastructure.query.QueryResult;
import pl.bodzioch.damian.service_provider.ServiceProviderDto;

public record GetServiceProviderByBurIdQueryResult(
        ServiceProviderDto serviceProviderDto
) implements QueryResult {
}
