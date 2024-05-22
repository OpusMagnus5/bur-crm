package pl.bodzioch.damian.service_provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.service_provider.query_dto.GetAllServiceProvidersQuery;
import pl.bodzioch.damian.service_provider.query_dto.GetAllServiceProvidersQueryResult;

@Component
@RequiredArgsConstructor
class GetAllServiceProvidersQueryHandler implements QueryHandler<GetAllServiceProvidersQuery, GetAllServiceProvidersQueryResult> {

    private final IProviderReadRepository readRepository;

    @Override
    public Class<GetAllServiceProvidersQuery> queryClass() {
        return GetAllServiceProvidersQuery.class;
    }

    @Override
    public GetAllServiceProvidersQueryResult handle(GetAllServiceProvidersQuery query) {
        return null; //TODO dokończyć
    }
}
