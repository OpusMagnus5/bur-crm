package pl.bodzioch.damian.user;

import pl.bodzioch.damian.valueobject.AuditDataDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserDto(
        Long id,
        UUID uuid,
        String email,
        String password,
        String firstName,
        String lastName,
        List<String> roles,
        LocalDateTime lastLogin,
        AuditDataDto auditData
) {

    UserDto(User user) {
        this(
                user.id(),
                user.uuid(),
                user.email(),
                user.password(),
                user.firstName(),
                user.lastName(),
                user.roles().stream().map(UserRole::name).toList(),
                user.lastLogin(),
                new AuditDataDto(
                        user.auditData().createdAt(),
                        user.auditData().modifiedAt(),
                        user.auditData().createdBy(),
                        user.auditData().modifiedBy()
                )
        );
    }
}
