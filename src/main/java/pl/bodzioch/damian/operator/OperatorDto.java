package pl.bodzioch.damian.operator;

import pl.bodzioch.damian.program.InnerProgramDto;
import pl.bodzioch.damian.user.InnerUserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record OperatorDto(
        Long id,
        UUID uuid,
        Integer version,
        String name,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Long modifiedBy,
        Long createdBy,
        List<InnerProgramDto> programs,
        InnerUserDto creator,
        InnerUserDto modifier
) {

    OperatorDto(Operator operator) {
        this(
                operator.id(),
                operator.uuid(),
                operator.version(),
                operator.name(),
                operator.notes(),
                operator.createdAt(),
                operator.modifiedAt(),
                operator.modifiedBy(),
                operator.createdBy(),
                operator.programs().stream()
                        .map(InnerProgramDto::new)
                        .toList(),
                new InnerUserDto(operator.creator()),
                new InnerUserDto(operator.modifier())
        );
    }

    public Optional<InnerUserDto> getCreator() {
        return Optional.ofNullable(creator);
    }

    public Optional<InnerUserDto>  getModifier() {
        return Optional.ofNullable(modifier);
    }
}
