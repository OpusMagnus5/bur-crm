package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.user.InnerUserDto;
import pl.bodzioch.damian.user.UserDto;
import pl.bodzioch.damian.utils.CipherComponent;

import java.io.Serializable;
import java.util.Optional;

public record UserListData(

        String id,
        String email,
        String firstName,
        String lastName,
        String role

) implements Serializable {

    public UserListData(UserDto userDto, CipherComponent cipher) {
        this(
                cipher.encryptMessage(Long.toString(userDto.id())),
                userDto.email(),
                userDto.firstName(),
                userDto.lastName(),
                userDto.roles().getLast().name()
        );
    }

    public UserListData(InnerUserDto userDto, CipherComponent cipher) {
        this(
                Optional.ofNullable(userDto).map(InnerUserDto::id)
                        .map(item -> cipher.encryptMessage(item.toString())).orElse(null),
                null,
                Optional.ofNullable(userDto).map(InnerUserDto::firstName).orElse(null),
                Optional.ofNullable(userDto).map(InnerUserDto::lastName).orElse(null),
                null
        );
    }
}
