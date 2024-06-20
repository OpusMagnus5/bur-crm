package pl.bodzioch.damian.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.user.command_dto.GenerateJwtTokenCommand;
import pl.bodzioch.damian.user.command_dto.GenerateJwtTokenCommandResult;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GenerateJwtTokenCommandHandler implements CommandHandler<GenerateJwtTokenCommand, GenerateJwtTokenCommandResult> {

	private final JwtEncoder jwtEncoder;

	@Override
	public Class<GenerateJwtTokenCommand> commandClass() {
		return GenerateJwtTokenCommand.class;
	}

	@Override
	public GenerateJwtTokenCommandResult handle(GenerateJwtTokenCommand command) {
		Authentication authentication = command.authentication();
		String roles = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(";"));
		Instant now = Instant.now();
		Instant expiresAt = now.plus(30, ChronoUnit.MINUTES);

		JwtClaimsSet claims = JwtClaimsSet.builder()
				.subject(authentication.getName())
				.issuer("self") //TODO zmienic na nazwe domeny
				.issuedAt(now)
				.expiresAt(expiresAt)
				.claim("roles", roles)
				.build();

		String tokenValue = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

		return new GenerateJwtTokenCommandResult(tokenValue, authentication, expiresAt);
	}
}
