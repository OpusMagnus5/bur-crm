package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.operator.OperatorDto;
import pl.bodzioch.damian.user.InnerUserDto;

import java.time.LocalDateTime;

public record GetOperatorDetailsResponse(
		String name,
		String notes,
		LocalDateTime createdAt,
		LocalDateTime modifiedAt,
		String creatorFirstName,
		String creatorLastName,
		String modifierFirstName,
		String modifierLastName
) {

	public GetOperatorDetailsResponse(OperatorDto operator) {
		this(
				operator.name(),
				operator.notes(),
				operator.createdAt(),
				operator.modifiedAt(),
				operator.getCreator().map(InnerUserDto::firstName).orElse(null),
				operator.getCreator().map(InnerUserDto::lastName).orElse(null),
				operator.getModifier().map(InnerUserDto::firstName).orElse(null),
				operator.getModifier().map(InnerUserDto::lastName).orElse(null)
		);
	}
}
