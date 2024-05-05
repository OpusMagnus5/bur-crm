package pl.bodzioch.damian.operator;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.operator.query_dto.GetOperatorByNameQuery;
import pl.bodzioch.damian.operator.query_dto.GetOperatorByNameQueryResult;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class GetOperatorByNameQueryHandler implements QueryHandler<GetOperatorByNameQuery, GetOperatorByNameQueryResult> {

    private final IOperatorReadRepository readRepository;

    @Override
    public Class<GetOperatorByNameQuery> queryClass() {
        return GetOperatorByNameQuery.class;
    }

    @Override
    public GetOperatorByNameQueryResult handle(GetOperatorByNameQuery query) {
        Optional<Operator> operator = readRepository.getByName(query.name());
        if (operator.isEmpty()) {
            throw buildOperatorByNameNotFound(query.name());
        }
        return new GetOperatorByNameQueryResult(new OperatorDto(operator.get()));
    }

    private AppException buildOperatorByNameNotFound(String name) {
        return new AppException(
                "Operator with name: " + name + " not found",
                HttpStatus.NOT_FOUND,
                List.of(new ErrorData("error.client.operatorByNameNotFound", List.of(name)))
        );
    }
}
