package pl.bodzioch.damian.user;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import pl.bodzioch.damian.valueobject.AuditData;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ToString
@Builder
@Getter
class User {

    private Long id;
    private UUID uuid;
    private Integer version;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private List<UserRole> roles;
    private LocalDateTime lastLogin;
    private AuditData auditData;

    User(CreateNewUserCommand command) {
        this.email = command.email();
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.password = generateFirstPassword();
        this.auditData = AuditData.builder()
                .createdBy(command.creatorId())
                .build();
        this.roles = List.of(UserRole.NOT_ACTIVE);
    }

    private String generateFirstPassword() {
        String passwordCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        int passwordLength = 12;
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < passwordLength; i++) {
            password.append(secureRandom.nextInt(passwordCharacters.length()));
        }
        return password.toString();
    }
}


