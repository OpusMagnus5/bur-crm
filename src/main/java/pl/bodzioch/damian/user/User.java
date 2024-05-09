package pl.bodzioch.damian.user;

import com.fasterxml.uuid.Generators;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.database.DbCaster;
import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbConstructor;
import pl.bodzioch.damian.infrastructure.database.DbManyToOne;
import pl.bodzioch.damian.user.command_dto.CreateNewUserCommand;
import pl.bodzioch.damian.utils.Encoder;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

record User (
         @DbColumn(name = "usr_id")
         Long id,
         @DbColumn(name = "usr_uuid")
         UUID uuid,
         @DbColumn(name = "usr_version")
         Integer version,
         @DbColumn(name = "usr_email")
         String email,
         @DbColumn(name = "usr_password")
         String password,
         @DbColumn(name = "usr_first_name")
         String firstName,
         @DbColumn(name = "usr_last_name")
         String lastName,
         @DbColumn(name = "usr_roles")
         List<UserRole> roles,
         @DbColumn(name = "usr_last_login")
         LocalDateTime lastLogin,
         @DbColumn(name = "usr_created_at")
         LocalDateTime createdAt,
         @DbColumn(name = "usr_modified_at")
         LocalDateTime modifiedAt,
         @DbColumn(name = "usr_modified_by")
         Long modifiedBy,
         @DbColumn(name = "usr_created_by")
         Long createdBy,
         @DbManyToOne(prefix = "creator")
         InnerUser creator,
         @DbManyToOne(prefix = "modifier")
         InnerUser modifier
) {

    @DbConstructor
    User {
    }

    User(CreateNewUserCommand command, String firstPassword) {
        this (
                null,
                Generators.timeBasedEpochGenerator().generate(),
                null,
                command.email(),
                Encoder.encodePassword(firstPassword),
                command.firstName(),
                command.lastName(),
                resolveRoles(command.role()),
                null,
                null,
                null,
                null,
                command.creatorId(),
                null,
                null
        );
    }

    static String generateFirstPassword() {
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

    Map<String, Object> getPropertySource() {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put("_usr_id", id);
        fields.put("_usr_uuid", uuid);
        fields.put("_usr_version", version);
        fields.put("_usr_email", email);
        fields.put("_usr_password", password);
        fields.put("_usr_first_name", firstName);
        fields.put("_usr_last_name", lastName);
        fields.put("_usr_roles", DbCaster.enumsToDb(roles));
        fields.put("_usr_last_login", lastLogin);
        fields.put("_usr_created_at", createdAt);
        fields.put("_usr_modified_at", modifiedAt);
        fields.put("_usr_modified_by", modifiedBy);
        fields.put("_usr_created_by", createdBy);
        return fields;
    }
}


