package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.program.InnerProgramDto;
import pl.bodzioch.damian.program.ProgramDto;
import pl.bodzioch.damian.utils.CipherComponent;

import java.io.Serializable;
import java.util.Optional;

public record ProgramData(
        String id,
        String name
) implements Serializable {

    public ProgramData(InnerProgramDto program) {
        this(
                null,
                Optional.ofNullable(program).map(InnerProgramDto::name).orElse(null)
        );
    }

    public ProgramData(ProgramDto program, CipherComponent cipher) {
        this(
                Optional.ofNullable(program).map(el -> cipher.encryptMessage(el.id().toString())).orElse(null),
                Optional.ofNullable(program).map(ProgramDto::name).orElse(null)
        );
    }
}
