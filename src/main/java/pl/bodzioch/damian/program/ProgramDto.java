package pl.bodzioch.damian.program;

import pl.bodzioch.damian.operator.InnerOperatorDto;
import pl.bodzioch.damian.user.InnerUserDto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProgramDto(
        Long id,
        UUID uuid,
        Integer version,
        String name,
        Long operatorId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Long createdBy,
        Long modifiedBy,
        InnerOperatorDto operator,
        InnerUserDto creator,
        InnerUserDto modifier
) {
    ProgramDto(Program program) {
        this(
                program.id(),
                program.uuid(),
                program.version(),
                program.name(),
                program.operatorId(),
                program.createdAt(),
                program.modifiedAt(),
                program.createdBy(),
                program.modifiedBy(),
                new InnerOperatorDto(program.operator()),
                new InnerUserDto(program.creator()),
                new InnerUserDto(program.modifier())
        );
    }
}
