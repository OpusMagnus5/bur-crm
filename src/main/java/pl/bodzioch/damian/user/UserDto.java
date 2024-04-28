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
                user.usr_id(),
                user.usr_uuid(),
                user.usr_version(),
                user.usr_email(),
                user.usr_password(),
                user.usr_first_name(),
                user.usr_last_name(),
                user.usr_roles().stream()
                        .map(UserRole::name)
                        .map(role -> new RoleDto(role, messageResolver.getMessage("user.role." + role)))
                        .toList(),
                user.usr_last_login(),
                user.usr_created_at(),
                user.usr_modified_at(),
                UserDto.toUserDto(user.creator(), messageResolver),
                UserDto.toUserDto(user.modifier(), messageResolver)
        );
    }

    private static UserDto toUserDto(User user, MessageResolver messageResolver) {
        return Optional.ofNullable(user)
                .map(userDto -> new UserDto(
                        user.usr_id(),
                        user.usr_uuid(),
                        user.usr_version(),
                        user.usr_email(),
                        user.usr_password(),
                        user.usr_first_name(),
                        user.usr_last_name(),
                        Optional.ofNullable(user.usr_roles())
                                .map(List::stream)
                                .map(stream -> stream.map(role -> new RoleDto(role.name(), messageResolver.getMessage("user.role." + role)))
                                .toList()).orElse(null),
                        user.usr_last_login(),
                        user.usr_created_at(),
                        user.usr_modified_at(),
                        null,
                        null
                ))
                .orElse(null);
    }
}
