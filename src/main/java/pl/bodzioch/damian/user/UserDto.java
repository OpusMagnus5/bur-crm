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
                user.getId(),
                user.getUuid(),
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoles().stream().map(UserRole::name).toList(),
                user.getLastLogin(),
                new AuditDataDto(
                        user.getAuditData().getCreatedAt(),
                        user.getAuditData().getModifiedAt(),
                        user.getAuditData().getCreatedBy(),
                        user.getAuditData().getModifiedBy()
                )
        );
    }
}
