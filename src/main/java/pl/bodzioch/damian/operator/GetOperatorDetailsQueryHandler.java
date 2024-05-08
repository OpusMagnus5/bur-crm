package pl.bodzioch.damian.operator;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.operator.query_dto.GetOperatorDetailsQuery;
import pl.bodzioch.damian.operator.query_dto.GetOperatorDetailsQueryResult;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetOperatorDetailsQueryHandler implements QueryHandler<GetOperatorDetailsQuery, GetOperatorDetailsQueryResult> {

	private final IOperatorReadRepository readRepository;

	@Override
	public Class<GetOperatorDetailsQuery> queryClass() {
		return GetOperatorDetailsQuery.class;
	}

	@Override
	public GetOperatorDetailsQueryResult handle(GetOperatorDetailsQuery query) {
		Operator operator = readRepository.getDetails(query.id()).orElseThrow(() -> buildOperatorByIdNotFound(query.id()));
		return new GetOperatorDetailsQueryResult(new OperatorDto(operator));
	}

	private AppException buildOperatorByIdNotFound(Long id) {
		return new AppException(
				"Not found opeartor with id: " + id,
				HttpStatus.NOT_FOUND,
				List.of(new ErrorData("error.client.operatorByIdNotFound", List.of(id.toString())))
		);
	}
}
