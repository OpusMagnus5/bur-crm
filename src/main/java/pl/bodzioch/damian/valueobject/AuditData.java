package pl.bodzioch.damian.valueobject;

import java.time.LocalDateTime;

public record AuditData(
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        Long createdBy,
        Long modifiedBy
) {
}
