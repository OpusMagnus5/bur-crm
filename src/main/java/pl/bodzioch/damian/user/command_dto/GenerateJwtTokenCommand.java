package pl.bodzioch.damian.user.command_dto;

import org.springframework.security.core.Authentication;
import pl.bodzioch.damian.infrastructure.command.Command;

public record GenerateJwtTokenCommand(
		Authentication authentication
) implements Command<GenerateJwtTokenCommandResult> {
}
