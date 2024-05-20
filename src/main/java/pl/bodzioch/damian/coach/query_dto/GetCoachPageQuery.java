package pl.bodzioch.damian.coach.query_dto;

import pl.bodzioch.damian.coach.CoachFilterField;
import pl.bodzioch.damian.infrastructure.query.Query;

import java.util.Map;

public record GetCoachPageQuery(
        int pageNumber,
        int pageSize,
        Map<CoachFilterField, ?> filters
) implements Query<GetCoachPageQueryResult> {
}
