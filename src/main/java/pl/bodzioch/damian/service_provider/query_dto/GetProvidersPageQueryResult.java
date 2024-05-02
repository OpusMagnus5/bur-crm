package pl.bodzioch.damian.service_provider.query_dto;

import pl.bodzioch.damian.infrastructure.query.QueryResult;
import pl.bodzioch.damian.service_provider.ServiceProviderDto;

import java.util.List;

public record GetProvidersPageQueryResult(
        List<ServiceProviderDto> providers,
        long totalProviders
) implements QueryResult {
}
