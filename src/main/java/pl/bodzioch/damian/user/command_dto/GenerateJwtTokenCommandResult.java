package pl.bodzioch.damian.user.command_dto;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import pl.bodzioch.damian.infrastructure.command.CommandResult;
import pl.bodzioch.damian.user.UserRole;

import java.time.Instant;
import java.util.List;

public record GenerateJwtTokenCommandResult(
		String token,
		String email,
		Instant expires,
		List<UserRole> roles
) implements CommandResult {

	public GenerateJwtTokenCommandResult(String token, Authentication authentication, Instant expires) {
		this(
				token,
				authentication.getName(),
				expires,
				authentication.getAuthorities().stream()
						.map(GrantedAuthority::getAuthority)
						.map(UserRole::valueOf)
						.toList()
		);
	}
}
