package pl.bodzioch.damian.user;

import com.fasterxml.uuid.Generators;
import org.springframework.http.HttpStatus;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbConstructor;
import pl.bodzioch.damian.infrastructure.database.DbId;
import pl.bodzioch.damian.infrastructure.database.DbManyToOne;
import pl.bodzioch.damian.user.command_dto.ChangeUserPasswordCommand;
import pl.bodzioch.damian.user.command_dto.CreateNewOrUpdateUserCommand;
import pl.bodzioch.damian.user.command_dto.ResetUserPasswordCommand;
import pl.bodzioch.damian.utils.Encoder;
import pl.bodzioch.damian.value_object.ErrorData;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static pl.bodzioch.damian.user.validator.PasswordValidator.ALLOWED_PASSWORD_CHARS;
import static pl.bodzioch.damian.user.validator.PasswordValidator.MIN_PASSWORD_LENGTH;

record User (
         @DbId
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

    User(CreateNewOrUpdateUserCommand command, String firstPassword, List<UserRole> creatorRoles) {
        this (
                command.id(),
                Generators.timeBasedEpochGenerator().generate(),
                command.version(),
                command.email(),
                Encoder.encodePassword(firstPassword),
                command.firstName(),
                command.lastName(),
                resolveRoles(command.role(), creatorRoles),
                null,
                null,
                null,
                null,
                command.creatorId(),
                null,
                null
        );
    }

    User(ResetUserPasswordCommand command, String newPassword) {
        this(
                command.id(), null, command.userVersion(), null, Encoder.encodePassword(newPassword),
                null, null, null, null, null, null, command.modifierId(),
                null, null, null
        );
    }

    private User(User user, ChangeUserPasswordCommand command) {
        this(
                user.id(), user.uuid(), command.version(), user.email(), Encoder.encodePassword(command.newPassword()),
                user.firstName(), user.lastName(), user.roles(), user.lastLogin(), user.createdAt(), user.modifiedAt(),
                command.userId(), user.createdBy(), user.creator(), user.modifier()
        );
    }

    User changePassword(ChangeUserPasswordCommand command) {
        boolean samePassword = Encoder.matches(command.newPassword(), this.password);
        boolean sameCurrentPassword = Encoder.matches(command.oldPassword(), this.password);
        if (samePassword) {
            throw buildTheSamePasswordAsOld();
        } else if (!sameCurrentPassword) {
            throw buildIncorrectOldPassword();
        }
        return new User(this, command);
    }

    static String generateNewPassword() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < MIN_PASSWORD_LENGTH; i++) {
            int index = secureRandom.nextInt(ALLOWED_PASSWORD_CHARS.length());
            password.append(ALLOWED_PASSWORD_CHARS.charAt(index));
        }
        return password.toString();
    }

    private static List<UserRole> resolveRoles(UserRole role, List<UserRole> creatorRoles) {
        checkRole(role, creatorRoles);
        switch (role) {
            case MANAGER -> {
                return List.of(UserRole.USER, UserRole.MANAGER);
            }
            case ADMIN -> {
                return List.of(UserRole.USER, UserRole.MANAGER, UserRole.ADMIN);
            }
            default -> {
                return List.of(role);
            }
        }
    }

    private static void checkRole(UserRole role, List<UserRole> creatorRoles) {
        creatorRoles.stream()
                .filter(creatorRole -> creatorRole.getHierarchy() >= role.getHierarchy())
                .findFirst()
                .orElseThrow(User::buildUserHasNoEnoughAuthorityException);
    }

    private static AppException buildUserHasNoEnoughAuthorityException() {
        return new AppException(
                HttpStatus.FORBIDDEN,
                List.of(new ErrorData("error.client.userHasNoEnoughAuthority", List.of()))
        );
    }

    static AppException buildUserByIdNotFound(Long id) {
        return new AppException(
                HttpStatus.NOT_FOUND,
                List.of(new ErrorData("error.client.userByIdNotFound", List.of(id.toString())))
        );
    }

    private static AppException buildTheSamePasswordAsOld() {
        return new AppException(
                HttpStatus.NOT_FOUND,
                List.of(new ErrorData("error.client.user.samePasswordAsOld", List.of()))
        );
    }

    private static AppException buildIncorrectOldPassword() {
        return new AppException(
                HttpStatus.NOT_FOUND,
                List.of(new ErrorData("error.client.user.incorrectOldPassword", List.of()))
        );
    }
}


