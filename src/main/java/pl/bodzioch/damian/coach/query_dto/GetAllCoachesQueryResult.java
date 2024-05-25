package pl.bodzioch.damian.coach.query_dto;

import pl.bodzioch.damian.coach.CoachDto;
import pl.bodzioch.damian.infrastructure.query.QueryResult;

import java.util.List;

public record GetAllCoachesQueryResult(
        List<CoachDto> coaches
) implements QueryResult {
}
