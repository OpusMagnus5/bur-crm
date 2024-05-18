package pl.bodzioch.damian.coach.query_dto;

import pl.bodzioch.damian.coach.CoachDto;
import pl.bodzioch.damian.infrastructure.query.QueryResult;

public record GetCoachByPeselQueryResult(
        CoachDto coachDto
) implements QueryResult {
}
