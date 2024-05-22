package pl.bodzioch.damian.intermediary;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.intermediary.query_dto.GetIntermediaryByNipQuery;
import pl.bodzioch.damian.intermediary.query_dto.GetIntermediaryByNipQueryResult;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetIntermediaryByNipQueryHandler implements QueryHandler<GetIntermediaryByNipQuery, GetIntermediaryByNipQueryResult> {

    private final IIntermediaryReadRepository readRepository;

    @Override
    public Class<GetIntermediaryByNipQuery> queryClass() {
        return GetIntermediaryByNipQuery.class;
    }

    @Override
    public GetIntermediaryByNipQueryResult handle(GetIntermediaryByNipQuery query) {
        Intermediary intermediary = readRepository.getByNip(query.nip()).orElseThrow(() -> buildIntermediaryByNipNotFound(query.nip()));
        return new GetIntermediaryByNipQueryResult(new IntermediaryDto(intermediary));
    }

    private AppException buildIntermediaryByNipNotFound(Long nip) {
        return new AppException(
                "Intermediary with nip: " + nip + " not found",
                HttpStatus.NOT_FOUND,
                List.of(new ErrorData("error.client.intermediaryByNipNotFound", List.of(nip.toString())))
        );
    }
}
