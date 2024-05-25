package pl.bodzioch.damian.program.query_dto;

import pl.bodzioch.damian.infrastructure.query.QueryResult;
import pl.bodzioch.damian.program.ProgramDto;

import java.util.List;

public record GetAllProgramsQueryResult(
        List<ProgramDto> programs
) implements QueryResult {
}
