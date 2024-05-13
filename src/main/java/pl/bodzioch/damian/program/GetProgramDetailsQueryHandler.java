package pl.bodzioch.damian.program;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.program.query_dto.GetProgramDetailsQuery;
import pl.bodzioch.damian.program.query_dto.GetProgramDetailsQueryResult;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetProgramDetailsQueryHandler implements QueryHandler<GetProgramDetailsQuery, GetProgramDetailsQueryResult> {

    private final IProgramReadRepository readRepository;

    @Override
    public Class<GetProgramDetailsQuery> queryClass() {
        return GetProgramDetailsQuery.class;
    }

    @Override
    public GetProgramDetailsQueryResult handle(GetProgramDetailsQuery query) {
        Program program = readRepository.getDetails(query.id()).orElseThrow(() -> buildProgramByIdNotFound(query.id()));
        return new GetProgramDetailsQueryResult(new ProgramDto(program));
    }

    private AppException buildProgramByIdNotFound(Long id) {
        return new AppException(
                "Not found program with id: " + id,
                HttpStatus.NOT_FOUND,
                List.of(new ErrorData("error.client.programByIdNotFound", List.of(id.toString())))
        );
    }
}
