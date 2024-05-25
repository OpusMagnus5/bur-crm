package pl.bodzioch.damian.coach.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetAllCoachesQuery() implements Query<GetAllCoachesQueryResult> {
}
