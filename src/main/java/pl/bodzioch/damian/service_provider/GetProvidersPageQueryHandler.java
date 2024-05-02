package pl.bodzioch.damian.service_provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.service_provider.query_dto.GetProvidersPageQuery;
import pl.bodzioch.damian.service_provider.query_dto.GetProvidersPageQueryResult;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetProvidersPageQueryHandler implements QueryHandler<GetProvidersPageQuery, GetProvidersPageQueryResult> {

    private final IProviderReadRepository readRepository;


    @Override
    public Class<GetProvidersPageQuery> queryClass() {
        return GetProvidersPageQuery.class;
    }

    @Override
    public GetProvidersPageQueryResult handle(GetProvidersPageQuery query) {
        PageQuery pageQuery = new PageQuery(query.pageNumber(), query.pageSize());
        PageQueryResult<ServiceProvider> result = readRepository.getPage(pageQuery);
        List<ServiceProviderDto> providers = result.elements().stream()
                .map(ServiceProviderDto::new)
                .toList();
        return new GetProvidersPageQueryResult(providers, result.totalElements());
    }
}
