package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.user.InnerUserDto;
import pl.bodzioch.damian.user.UserDto;
import pl.bodzioch.damian.utils.CipherComponent;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public record GetUserByIdResponse(

		String id,
		String email,
		Integer version,
		String firstName,
		String lastName,
		List<RoleDto> roles,
		LocalDateTime lastLogin,
		LocalDateTime createdAt,
		LocalDateTime modifiedAt,
		String creatorFirstName,
		String creatorLastName,
		String modifierFirstName,
		String modifierLastName

) implements Serializable {

	public GetUserByIdResponse(UserDto userDto, CipherComponent cipher) {
		this(
				cipher.encryptMessage(userDto.id().toString()),
				userDto.email(),
				userDto.version(),
				userDto.firstName(),
				userDto.lastName(),
				userDto.roles(),
				userDto.lastLogin(),
				userDto.createdAt(),
				userDto.modifiedAt(),
				Optional.ofNullable(userDto.creator()).map(InnerUserDto::firstName).orElse(null),
				Optional.ofNullable(userDto.creator()).map(InnerUserDto::lastName).orElse(null),
				Optional.ofNullable(userDto.modifier()).map(InnerUserDto::firstName).orElse(null),
				Optional.ofNullable(userDto.modifier()).map(InnerUserDto::lastName).orElse(null)
		);
	}
}
