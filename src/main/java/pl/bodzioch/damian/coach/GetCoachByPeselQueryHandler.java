package pl.bodzioch.damian.coach;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.coach.query_dto.GetCoachByPeselQuery;
import pl.bodzioch.damian.coach.query_dto.GetCoachByPeselQueryResult;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetCoachByPeselQueryHandler implements QueryHandler<GetCoachByPeselQuery, GetCoachByPeselQueryResult> {

    private final ICoachReadRepository readRepository;

    @Override
    public Class<GetCoachByPeselQuery> queryClass() {
        return GetCoachByPeselQuery.class;
    }

    @Override
    public GetCoachByPeselQueryResult handle(GetCoachByPeselQuery query) {
        Coach coach = readRepository.getByNip(query.pesel()).orElseThrow(() -> buildCoachByPeselNotFound(query.pesel()));
        return new GetCoachByPeselQueryResult(new CoachDto(coach));
    }

    private AppException buildCoachByPeselNotFound(String pesel) {
        return new AppException(
                "Coach with PESEL: " + pesel + " not found",
                HttpStatus.NOT_FOUND,
                List.of(new ErrorData("error.client.coach.coachByPeselNotFound", List.of(pesel)))
        );
    }
}
