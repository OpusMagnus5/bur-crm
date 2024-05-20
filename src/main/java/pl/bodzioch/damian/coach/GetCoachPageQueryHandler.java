package pl.bodzioch.damian.coach;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.coach.query_dto.GetCoachPageQuery;
import pl.bodzioch.damian.coach.query_dto.GetCoachPageQueryResult;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetCoachPageQueryHandler implements QueryHandler<GetCoachPageQuery, GetCoachPageQueryResult> {

    private final ICoachReadRepository readRepository;

    @Override
    public Class<GetCoachPageQuery> queryClass() {
        return GetCoachPageQuery.class;
    }

    @Override
    public GetCoachPageQueryResult handle(GetCoachPageQuery query) {
        PageQuery pageQuery = new PageQuery(query.pageNumber(), query.pageSize(), query.filters());
        PageQueryResult<Coach> result = readRepository.getPage(pageQuery);
        List<CoachDto> coaches = result.elements().stream()
                .map(CoachDto::new)
                .toList();
        return new GetCoachPageQueryResult(coaches, result.totalElements());
    }
}
