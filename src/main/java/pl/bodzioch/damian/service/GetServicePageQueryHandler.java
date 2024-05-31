package pl.bodzioch.damian.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.service.query_dto.GetServicePageQuery;
import pl.bodzioch.damian.service.query_dto.GetServicePageQueryResult;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetServicePageQueryHandler implements QueryHandler<GetServicePageQuery, GetServicePageQueryResult> {

    private final IServiceReadRepository readRepository;

    @Override
    public Class<GetServicePageQuery> queryClass() {
        return GetServicePageQuery.class;
    }

    @Override
    public GetServicePageQueryResult handle(GetServicePageQuery query) {
        PageQuery pageQuery = new PageQuery(query.pageNumber(), query.pageSize(), query.filters());
        PageQueryResult<Service> result = readRepository.getPage(pageQuery);
        List<ServiceDto> services = result.elements().stream()
                .map(ServiceDto::new)
                .toList();
        return new GetServicePageQueryResult(services, result.totalElements());
    }
}
