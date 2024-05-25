package pl.bodzioch.damian.dto;

import java.io.Serializable;
import java.util.List;

public record GetAllProgramsResponse(
        List<ProgramData> programs
) implements Serializable {
}
