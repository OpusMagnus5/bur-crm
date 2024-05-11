package pl.bodzioch.damian.program;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.program.query_dto.GetProgramByNameQuery;
import pl.bodzioch.damian.program.query_dto.GetProgramByNameQueryResult;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetProgramByNameQueryHandler implements QueryHandler<GetProgramByNameQuery, GetProgramByNameQueryResult> {

    private final IProgramReadRepository readRepository;

    @Override
    public Class<GetProgramByNameQuery> queryClass() {
        return GetProgramByNameQuery.class;
    }

    @Override
    public GetProgramByNameQueryResult handle(GetProgramByNameQuery query) {
        Program program = readRepository.getByName(query.name())
                .orElseThrow(() -> buildProgramByNameNotFound(query.name()));
        return new GetProgramByNameQueryResult(new ProgramDto(program));
    }

    private AppException buildProgramByNameNotFound(String name) {
        return new AppException(
                "Program with name: " + name + " not found",
                HttpStatus.NOT_FOUND,
                List.of(new ErrorData("error.client.programByNameNotFound", List.of(name)))
        );
    }
}
