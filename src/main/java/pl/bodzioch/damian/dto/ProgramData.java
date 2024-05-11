package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.program.InnerProgramDto;

import java.io.Serializable;
import java.util.Optional;

public record ProgramData(
        String name
) implements Serializable {

    public ProgramData(InnerProgramDto program) {
        this(
                Optional.ofNullable(program).map(InnerProgramDto::name).orElse(null)
        );
    }
}
