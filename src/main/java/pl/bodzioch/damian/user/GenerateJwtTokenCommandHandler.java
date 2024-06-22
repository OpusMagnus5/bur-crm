package pl.bodzioch.damian.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.configuration.security.UserDetailsDto;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.user.command_dto.GenerateJwtTokenCommand;
import pl.bodzioch.damian.user.command_dto.GenerateJwtTokenCommandResult;
import pl.bodzioch.damian.utils.CipherComponent;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GenerateJwtTokenCommandHandler implements CommandHandler<GenerateJwtTokenCommand, GenerateJwtTokenCommandResult> {

	public static final String BEARER_COOKIE = "bearer";
	public static final String ROLES_CLAIM = "roles";
	public static final String PRINCIPAL_ID = "principal_id";
	public static final String AUTHORITIES_CLAIM_DELIMITER = ";";

	private final JwtEncoder jwtEncoder;
	private final CipherComponent cipher;

	@Override
	public Class<GenerateJwtTokenCommand> commandClass() {
		return GenerateJwtTokenCommand.class;
	}

	@Override
	public GenerateJwtTokenCommandResult handle(GenerateJwtTokenCommand command) {
		Authentication authentication = command.authentication();
		String roles = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(AUTHORITIES_CLAIM_DELIMITER));
		Instant now = Instant.now();
		Instant expiresAt = now.plus(30, ChronoUnit.MINUTES);
		String id = cipher.encryptMessage(((UserDetailsDto) authentication.getPrincipal()).getId().toString());

		JwtClaimsSet claims = JwtClaimsSet.builder()
				.subject(authentication.getName())
				.issuer("self") //TODO zmienic na nazwe domeny
				.issuedAt(now)
				.expiresAt(expiresAt)
				.claim(ROLES_CLAIM, roles)
				.claim(PRINCIPAL_ID, id)
				.build();

		String tokenValue = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

		return new GenerateJwtTokenCommandResult(tokenValue, authentication, expiresAt);
	}
}
