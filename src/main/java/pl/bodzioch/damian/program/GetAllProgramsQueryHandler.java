package pl.bodzioch.damian.program;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.program.query_dto.GetAllProgramsQuery;
import pl.bodzioch.damian.program.query_dto.GetAllProgramsQueryResult;

import java.util.List;

@Cacheable("programs")
@Component
@RequiredArgsConstructor
public class GetAllProgramsQueryHandler implements QueryHandler<GetAllProgramsQuery, GetAllProgramsQueryResult> {

    private final IProgramReadRepository readRepository;

    @Override
    public Class<GetAllProgramsQuery> queryClass() {
        return GetAllProgramsQuery.class;
    }

    @Override
    public GetAllProgramsQueryResult handle(GetAllProgramsQuery query) {
        List<ProgramDto> programs = readRepository.getAll().stream()
                .map(ProgramDto::new)
                .toList();
        return new GetAllProgramsQueryResult(programs);
    }
}
