package pl.bodzioch.damian.coach;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.coach.query_dto.GetCoachDetailsQuery;
import pl.bodzioch.damian.coach.query_dto.GetCoachDetailsQueryResult;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetCoachDetailsQueryHandler implements QueryHandler<GetCoachDetailsQuery, GetCoachDetailsQueryResult> {

	private final ICoachReadRepository readRepository;

	@Override
	public Class<GetCoachDetailsQuery> queryClass() {
		return GetCoachDetailsQuery.class;
	}

	@Override
	public GetCoachDetailsQueryResult handle(GetCoachDetailsQuery query) {
		Coach coach = readRepository.getDetails(query.id()).orElseThrow(() -> buildCoachByIdNotFound(query.id()));
		return new GetCoachDetailsQueryResult(new CoachDto(coach));
	}

	private AppException buildCoachByIdNotFound(Long id) {
		return new AppException(
				"Not found coach with id: " + id,
				HttpStatus.NOT_FOUND,
				List.of(new ErrorData("error.client.coach.coachByIdNotFound", List.of(id.toString())))
		);
	}
}
