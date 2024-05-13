package pl.bodzioch.damian.program.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetProgramDetailsQuery(
        Long id
) implements Query<GetProgramDetailsQueryResult> {
}
