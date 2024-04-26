package pl.bodzioch.damian.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        UserDto creator,
        UserDto modifier
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
                user.createdAt(),
                user.modifiedAt(),
                UserDto.toUserDto(user.creator()),
                UserDto.toUserDto(user.modifier())
        );
    }

    private static UserDto toUserDto(User user) {
        return Optional.ofNullable(user)
                .map(userDto -> new UserDto(
                        user.id(),
                        user.uuid(),
                        user.email(),
                        user.password(),
                        user.firstName(),
                        user.lastName(),
                        user.roles().stream().map(UserRole::name).toList(),
                        user.lastLogin(),
                        user.createdAt(),
                        user.modifiedAt(),
                        null,
                        null
                ))
                .orElse(null);
    }
}
