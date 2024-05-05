package pl.bodzioch.damian.operator;

import pl.bodzioch.damian.user.InnerUserDto;

import java.time.LocalDateTime;
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
        InnerUserDto creator,
        InnerUserDto modifier
) {

    OperatorDto(Operator operator) {
        this(
                operator.opr_id(),
                operator.opr_uuid(),
                operator.opr_version(),
                operator.opr_name(),
                operator.opr_notes(),
                operator.opr_created_at(),
                operator.opr_modified_at(),
                operator.opr_modified_by(),
                operator.opr_created_by(),
                new InnerUserDto(operator.creator()),
                new InnerUserDto(operator.modifier())
        );
    }
}
