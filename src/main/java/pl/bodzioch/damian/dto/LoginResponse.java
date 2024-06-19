package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record LoginResponse(
		String token
) implements Serializable {
}
