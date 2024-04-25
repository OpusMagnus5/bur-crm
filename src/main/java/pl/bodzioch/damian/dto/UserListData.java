package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.user.UserDto;
import pl.bodzioch.damian.utils.CipherComponent;

import java.io.Serializable;

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
                userDto.roles().getLast()
        );
    }
}
