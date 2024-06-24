package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.user.UserRole;
import pl.bodzioch.damian.user.command_dto.GenerateJwtTokenCommandResult;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public record LoginResponse(
		String id,
		String email,
		Instant expires,
		List<UserRole> roles
) implements Serializable {

	public LoginResponse(GenerateJwtTokenCommandResult commandResult) {
		this(
				commandResult.id(),
				commandResult.email(),
				commandResult.expires(),
				commandResult.roles()
		);
	}
}
