package pl.bodzioch.damian.dto;

import java.io.Serializable;
import java.util.List;

public record ProgramPageResponse(
        List<ProgramData> programs,
        Long totalPrograms
) implements Serializable {
}
