package pl.bodzioch.damian.user;

import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.user.commandDto.CreateNewUserCommand;

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
         LocalDateTime createdAt,
         LocalDateTime modifiedAt,
         User creator,
         User modifier
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
                resolveRoles(command.role()),
                null,
                null,
                null,
                new User(command.creatorId(), null, null, null, null, null, null, null, null, null, null, null, null),
                null
        );
    }

    private static String generateFirstPassword() {
        String passwordCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        int passwordLength = 12;
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < passwordLength; i++) {
            int index = secureRandom.nextInt(passwordCharacters.length());
            password.append(passwordCharacters.charAt(index));
        }
        return password.toString();
    }

    private static List<UserRole> resolveRoles(String role) {
        UserRole userRole = UserRole.valueOf(role);
        switch (userRole) {
            case USER -> {
                return List.of(UserRole.USER);
            }
            case MANAGER -> {
                return List.of(UserRole.USER, UserRole.MANAGER);
            }
            case ADMIN -> {
                return List.of(UserRole.USER, UserRole.MANAGER, UserRole.ADMIN);
            }
        }
        throw AppException.getGeneralError();
    }
}


