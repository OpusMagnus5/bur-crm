package pl.bodzioch.damian.program.query_dto;

import pl.bodzioch.damian.infrastructure.query.QueryResult;
import pl.bodzioch.damian.program.ProgramDto;

public record GetProgramDetailsQueryResult(
        ProgramDto program
) implements QueryResult {
}
