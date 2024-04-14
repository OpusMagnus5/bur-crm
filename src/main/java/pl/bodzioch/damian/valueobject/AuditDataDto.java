package pl.bodzioch.damian.valueobject;

import java.time.LocalDateTime;

public record AuditDataDto(

     LocalDateTime createdAt,
     LocalDateTime modifiedAt,
     Long createdBy,
     Long modifiedBy
){
}