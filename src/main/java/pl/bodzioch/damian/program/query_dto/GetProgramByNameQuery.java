package pl.bodzioch.damian.program.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetProgramByNameQuery(
        String name,
        Long operatorId
) implements Query<GetProgramByNameQueryResult> {
}
