package pl.bodzioch.damian.intermediary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.intermediary.query_dto.GetIntermediaryPageQuery;
import pl.bodzioch.damian.intermediary.query_dto.GetIntermediaryPageQueryResult;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetIntermediaryPageQueryHandler implements QueryHandler<GetIntermediaryPageQuery, GetIntermediaryPageQueryResult> {

    private final IIntermediaryReadRepository readRepository;


    @Override
    public Class<GetIntermediaryPageQuery> queryClass() {
        return GetIntermediaryPageQuery.class;
    }

    @Override
    public GetIntermediaryPageQueryResult handle(GetIntermediaryPageQuery query) {
        PageQuery pageQuery = new PageQuery(query.pageNumber(), query.pageSize(), query.filters());
        PageQueryResult<Intermediary> result = readRepository.getPage(pageQuery);
        List<IntermediaryDto> intermediaries = result.elements().stream()
                .map(IntermediaryDto::new)
                .toList();
        return new GetIntermediaryPageQueryResult(intermediaries, result.totalElements());
    }
}
