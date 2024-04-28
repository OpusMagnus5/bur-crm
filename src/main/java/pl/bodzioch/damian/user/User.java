package pl.bodzioch.damian.user;

import com.fasterxml.uuid.Generators;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.user.commandDto.CreateNewUserCommand;
import pl.bodzioch.damian.utils.Encoder;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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



    User(CreateNewUserCommand command) {
        this (
                null,
                Generators.timeBasedEpochGenerator().generate(),
                null,
                command.email(),
                Encoder.encodePassword(generateFirstPassword()),
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

    Map<String, Object> getPropertySource() {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put("_usr_id", usr_id);
        fields.put("_usr_uuid", usr_uuid);
        fields.put("_usr_version", usr_version);
        fields.put("_usr_email", usr_email);
        fields.put("_usr_password", usr_password);
        fields.put("_usr_first_name", usr_first_name);
        fields.put("_usr_last_name", usr_last_name);
        fields.put("_usr_roles", usr_roles.stream().map(Enum::name).collect(Collectors.joining(";")));
        fields.put("_usr_last_login", usr_last_login);
        fields.put("_usr_created_at", usr_created_at);
        fields.put("_usr_modified_at", usr_modified_at);
        fields.put("_usr_modified_by", usr_modified_by);
        fields.put("_usr_created_by", usr_created_by);
        return fields;
    }

    @SuppressWarnings("unchecked")
    static List<User> fromProperties(Map<String, Object> properties, String cursorName) {
        ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) properties.get(cursorName);
        ArrayList<User> users = new ArrayList<>();
        for (Map<String, Object> record : list) {
            User user = buildUser(record);
            users.add(user);
        }
        return users;
    }

    private static User buildUser(Map<String, Object> record) {
        return new User(
                (Long) record.get("usr_id"),
                (UUID) record.get("usr_uuid"),
                (Integer) record.get("usr_version"),
                (String) record.get("usr_email"),
                (String) record.get("usr_password"),
                (String) record.get("usr_first_name"),
                (String) record.get("usr_last_name"),
                Arrays.stream(((String) record.get("usr_roles")).split(";"))
                        .map(UserRole::valueOf)
                        .toList(),
                record.get("usr_last_login") instanceof Timestamp ? ((Timestamp) record.get("usr_last_login")).toLocalDateTime() : null,
                ((Timestamp) record.get("usr_created_at")).toLocalDateTime(),
                record.get("usr_modified_at") instanceof Timestamp ? ((Timestamp) record.get("usr_modified_at")).toLocalDateTime() : null,
                record.get("usr_modified_by") instanceof Long ? (Long) record.get("usr_modified_by") : null,
                record.get("usr_created_by") instanceof Long ? (Long) record.get("usr_created_by") : null,
                buildCreator(record),
                buildModifier(record)
        );
    }

    private static User buildCreator(Map<String, Object> record) {
        return new User(
                null,
                null,
                null,
                null,
                null,
                record.get("creator_usr_first_name") instanceof String ? (String) record.get("creator_usr_first_name") : null,
                record.get("creator_usr_last_name") instanceof String ? (String) record.get("creator_usr_last_name") : null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private static User buildModifier(Map<String, Object> record) {
        return new User(
                null,
                null,
                null,
                null,
                null,
                record.get("modifier_usr_first_name") instanceof String ? (String) record.get("modifier_usr_first_name") : null,
                record.get("modifier_usr_last_name") instanceof String ? (String) record.get("modifier_usr_last_name") : null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}


