package pl.bodzioch.damian.user;

import pl.bodzioch.damian.dto.RoleDto;
import pl.bodzioch.damian.utils.MessageResolver;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record UserDto(
        Long id,
        UUID uuid,
        Integer version,
        String email,
        String password,
        String firstName,
        String lastName,
        List<RoleDto> roles,
        LocalDateTime lastLogin,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        UserDto creator,
        UserDto modifier
) {

    UserDto(User user, MessageResolver messageResolver) {
        this(
                user.id(),
                user.uuid(),
                user.version(),
                user.email(),
                user.password(),
                user.firstName(),
                user.lastName(),
                user.roles().stream()
                        .map(UserRole::name)
                        .map(role -> new RoleDto(role, messageResolver.getMessage("user.role." + role)))
                        .toList(),
                user.lastLogin(),
                user.createdAt(),
                user.modifiedAt(),
                UserDto.toUserDto(user.creator(), messageResolver),
                UserDto.toUserDto(user.modifier(), messageResolver)
        );
    }

    private static UserDto toUserDto(User user, MessageResolver messageResolver) {
        return Optional.ofNullable(user)
                .map(userDto -> new UserDto(
                        user.id(),
                        user.uuid(),
                        user.version(),
                        user.email(),
                        user.password(),
                        user.firstName(),
                        user.lastName(),
                        user.roles().stream()
                                .map(UserRole::name)
                                .map(role -> new RoleDto(role, messageResolver.getMessage("user.role." + role)))
                                .toList(),
                        user.lastLogin(),
                        user.createdAt(),
                        user.modifiedAt(),
                        null,
                        null
                ))
                .orElse(null);
    }
}
