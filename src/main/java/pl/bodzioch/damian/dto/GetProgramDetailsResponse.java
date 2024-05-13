package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.program.ProgramDto;
import pl.bodzioch.damian.user.InnerUserDto;
import pl.bodzioch.damian.utils.CipherComponent;

import java.time.LocalDateTime;
import java.util.Optional;

public record GetProgramDetailsResponse(
        String id,
        Integer version,
        String name,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        String creatorFirstName,
        String creatorLastName,
        String modifierFirstName,
        String modifierLastName,
        OperatorData operator
) {

    public GetProgramDetailsResponse(ProgramDto program, CipherComponent cipher) {
        this(
                cipher.encryptMessage(program.id().toString()),
                program.version(),
                program.name(),
                program.createdAt(),
                program.modifiedAt(),
                program.creator().firstName(),
                program.creator().lastName(),
                Optional.ofNullable(program.modifier()).map(InnerUserDto::firstName).orElse(null),
                Optional.ofNullable(program.modifier()).map(InnerUserDto::lastName).orElse(null),
                new OperatorData(program.operator())
        );
    }
}
