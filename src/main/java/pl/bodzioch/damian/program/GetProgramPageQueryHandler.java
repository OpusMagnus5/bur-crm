package pl.bodzioch.damian.program;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.program.query_dto.GetProgramPageQuery;
import pl.bodzioch.damian.program.query_dto.GetProgramPageQueryResult;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetProgramPageQueryHandler implements QueryHandler<GetProgramPageQuery, GetProgramPageQueryResult> {

    private final IProgramReadRepository readRepository;

    @Override
    public Class<GetProgramPageQuery> queryClass() {
        return GetProgramPageQuery.class;
    }

    @Override
    public GetProgramPageQueryResult handle(GetProgramPageQuery query) {
        PageQuery pageQuery = new PageQuery(query.pageNumber(), query.pageSize(), query.filters());
        PageQueryResult<Program> result = readRepository.getPage(pageQuery);
        List<ProgramDto> programs = result.elements().stream()
                .map(ProgramDto::new)
                .toList();
        return new GetProgramPageQueryResult(programs, result.totalElements());
    }
}
