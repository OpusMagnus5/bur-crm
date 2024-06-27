package pl.bodzioch.damian.error;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.dto.SaveWebErrorRequest;
import pl.bodzioch.damian.error.command_dto.SaveErrorCommand;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.utils.CipherComponent;

@RestController
@RequestMapping("/api/error")
@RequiredArgsConstructor
@Validated
class ErrorController {

	private final CommandExecutor commandExecutor;
	private final CipherComponent cipher;

	@PreAuthorize("hasAuthority('USER')")
	@PostMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void saveError(@Valid @RequestBody SaveWebErrorRequest request) {
		Long userId = cipher.getPrincipalIdIfExists().orElse(null);
		ErrorDto errorDto = new ErrorDto(request, userId);
		SaveErrorCommand command = new SaveErrorCommand(errorDto);
		commandExecutor.executeAsync(command);
	}
}
