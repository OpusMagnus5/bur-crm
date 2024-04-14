package pl.bodzioch.damian.user;

import pl.bodzioch.damian.valueobject.AuditData;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

record User (
         Long id,
         UUID uuid,
         Integer version,
         String email,
         String password,
         String firstName,
         String lastName,
         List<UserRole> roles,
         LocalDateTime lastLogin,
         AuditData auditData
) {



    User(CreateNewUserCommand command) {
        this (
                null,
                null,
                null,
                command.email(),
                generateFirstPassword(),
                command.firstName(),
                command.lastName(),
                List.of(UserRole.NOT_ACTIVE),
                null,
                new AuditData(null, null, command.creatorId(), null)
        );
    }

    private static String generateFirstPassword() {
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


