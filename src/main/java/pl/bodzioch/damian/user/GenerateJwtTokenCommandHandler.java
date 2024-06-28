package pl.bodzioch.damian.user;

import com.fasterxml.uuid.Generators;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.configuration.security.SecurityConstants;
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
class GenerateJwtTokenCommandHandler implements CommandHandler<GenerateJwtTokenCommand, GenerateJwtTokenCommandResult> {

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
				.collect(Collectors.joining(SecurityConstants.AUTHORITIES_CLAIM_DELIMITER));
		Instant now = Instant.now();
		Instant expiresAt = now.plus(30, ChronoUnit.MINUTES);
		String id = cipher.encryptMessage(((UserDetailsDto) authentication.getPrincipal()).getId().toString());

		JwtClaimsSet claims = JwtClaimsSet.builder()
				.subject(authentication.getName())
				.issuer("self") //TODO zmienic na nazwe domeny
				.issuedAt(now)
				.expiresAt(expiresAt)
				.claim(SecurityConstants.ROLES_CLAIM, roles)
				.claim(SecurityConstants.PRINCIPAL_ID, id)
				.claim(SecurityConstants.SESSION_ID, Generators.timeBasedEpochGenerator().generate().toString())
				.build();

		String tokenValue = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

		return new GenerateJwtTokenCommandResult(tokenValue, authentication, expiresAt, id);
	}
}
