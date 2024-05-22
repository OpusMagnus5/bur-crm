package pl.bodzioch.damian.intermediary;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.intermediary.query_dto.GetIntermediaryDetailsQuery;
import pl.bodzioch.damian.intermediary.query_dto.GetIntermediaryDetailsQueryResult;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetIntermediaryDetailsQueryHandler implements QueryHandler<GetIntermediaryDetailsQuery, GetIntermediaryDetailsQueryResult> {

    private final IIntermediaryReadRepository readRepository;

    @Override
    public Class<GetIntermediaryDetailsQuery> queryClass() {
        return GetIntermediaryDetailsQuery.class;
    }

    @Override
    public GetIntermediaryDetailsQueryResult handle(GetIntermediaryDetailsQuery command) {
        Intermediary intermediary = readRepository.getDetails(command.id()).orElseThrow(() -> buildProgramByIdNotFound(command.id()));
        return new GetIntermediaryDetailsQueryResult(new IntermediaryDto(intermediary));
    }

    private AppException buildProgramByIdNotFound(Long id) {
        return new AppException(
                "Not found intermediary with id: " + id,
                HttpStatus.NOT_FOUND,
                List.of(new ErrorData("error.client.intermediaryByIdNotFound", List.of(id.toString())))
        );
    }
}
