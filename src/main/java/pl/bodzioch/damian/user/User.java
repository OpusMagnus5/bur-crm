package pl.bodzioch.damian.user;

import com.fasterxml.uuid.Generators;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.user.command_dto.CreateNewUserCommand;
import pl.bodzioch.damian.utils.DbCaster;
import pl.bodzioch.damian.utils.Encoder;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

record User (
         Long usr_id,
         UUID usr_uuid,
         Integer usr_version,
         String usr_email,
         String usr_password,
         String usr_first_name,
         String usr_last_name,
         List<UserRole> usr_roles,
         LocalDateTime usr_last_login,
         LocalDateTime usr_created_at,
         LocalDateTime usr_modified_at,
         Long usr_modified_by,
         Long usr_created_by,
         User creator,
         User modifier
) {



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

    private User(String firstName, String lastName) {
        this (
                null, null, null, null, null, firstName, lastName, null,
                null, null, null, null, null, null,
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
        fields.put("_usr_id", usr_id);
        fields.put("_usr_uuid", usr_uuid);
        fields.put("_usr_version", usr_version);
        fields.put("_usr_email", usr_email);
        fields.put("_usr_password", usr_password);
        fields.put("_usr_first_name", usr_first_name);
        fields.put("_usr_last_name", usr_last_name);
        fields.put("_usr_roles", DbCaster.enumsToDb(usr_roles));
        fields.put("_usr_last_login", usr_last_login);
        fields.put("_usr_created_at", usr_created_at);
        fields.put("_usr_modified_at", usr_modified_at);
        fields.put("_usr_modified_by", usr_modified_by);
        fields.put("_usr_created_by", usr_created_by);
        return fields;
    }

    static List<User> fromProperties(Map<String, Object> properties, String cursorName) {
        return DbCaster.fromProperties(User::buildUser, properties, cursorName);
    }

    private static User buildUser(Map<String, Object> record) {
        return new User(
                DbCaster.cast(Long.class, record.get("usr_id")),
                DbCaster.cast(UUID.class, record.get("usr_uuid")),
                DbCaster.cast(Integer.class, record.get("usr_version")),
                DbCaster.cast(String.class, record.get("usr_email")),
                DbCaster.cast(String.class, record.get("usr_password")),
                DbCaster.cast(String.class, record.get("usr_first_name")),
                DbCaster.cast(String.class, record.get("usr_last_name")),
                DbCaster.toEnums(UserRole.class, record.get("usr_roles")),
                DbCaster.mapTimestamp(record.get("usr_last_login")),
                DbCaster.mapTimestamp(record.get("usr_created_at")),
                DbCaster.mapTimestamp(record.get("usr_modified_at")),
                DbCaster.cast(Long.class, record.get("usr_modified_by")),
                DbCaster.cast(Long.class, record.get("usr_created_by")),
                buildCreator(record),
                buildModifier(record)
        );
    }

    private static User buildCreator(Map<String, Object> record) {
        return new User(
                DbCaster.cast(String.class, record.get("creator_usr_first_name")),
                DbCaster.cast(String.class, record.get("creator_usr_last_name"))
        );
    }

    private static User buildModifier(Map<String, Object> record) {
        return new User(
                DbCaster.cast(String.class, record.get("modifier_usr_first_name")),
                DbCaster.cast(String.class, record.get("modifier_usr_last_name"))
        );
    }
}


