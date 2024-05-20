package pl.bodzioch.damian.coach.query_dto;

import pl.bodzioch.damian.coach.CoachDto;
import pl.bodzioch.damian.infrastructure.query.QueryResult;

import java.util.List;

public record GetCoachPageQueryResult(
        List<CoachDto> coaches,
        long totalCoaches
) implements QueryResult {
}
