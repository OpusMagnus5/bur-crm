package pl.bodzioch.damian.service_provider.query_dto;

import pl.bodzioch.damian.infrastructure.query.QueryResult;
import pl.bodzioch.damian.service_provider.ServiceProviderDto;

import java.util.List;

public record GetAllServiceProvidersQueryResult(
        List<ServiceProviderDto> serviceProviders
) implements QueryResult {
}
