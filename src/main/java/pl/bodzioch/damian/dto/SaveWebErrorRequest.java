package pl.bodzioch.damian.dto;

import jakarta.validation.constraints.NotEmpty;

public record SaveWebErrorRequest(
		@NotEmpty
		String clazz,
		@NotEmpty
		String message,
		@NotEmpty
		String stacktrace
) {
}
