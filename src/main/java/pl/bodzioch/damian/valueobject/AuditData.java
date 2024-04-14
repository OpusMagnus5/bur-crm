package pl.bodzioch.damian.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;

@Getter
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditData {

    @CurrentTimestamp(event = EventType.INSERT, source = SourceType.VM)
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @CurrentTimestamp(event = EventType.UPDATE, source = SourceType.VM)
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    @Column(name = "created_by")
    private Long createdBy;
    @Column(name = "modified_by")
    private Long modifiedBy;

}