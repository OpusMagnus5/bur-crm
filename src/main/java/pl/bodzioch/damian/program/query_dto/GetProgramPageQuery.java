package pl.bodzioch.damian.program.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;
import pl.bodzioch.damian.program.ProgramFilterField;

import java.util.Map;

public record GetProgramPageQuery(
        int pageNumber,
        int pageSize,
        Map<ProgramFilterField, ?> filters
) implements Query<GetProgramPageQueryResult> {
}
