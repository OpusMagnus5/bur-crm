package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.coach.CoachDto;
import pl.bodzioch.damian.user.InnerUserDto;
import pl.bodzioch.damian.utils.CipherComponent;

import java.time.LocalDateTime;
import java.util.Optional;

public record CoachDetailsResponse(
		String id,
		Integer version,
		String firstName,
		String lastName,
		String pesel,
		LocalDateTime createdAt,
		LocalDateTime modifiedAt,
		String creatorFirstName,
		String creatorLastName,
		String modifierFirstName,
		String modifierLastName
) {

	public CoachDetailsResponse(CoachDto coach, CipherComponent cipher) {
		this(
				cipher.encryptMessage(coach.id().toString()),
				coach.version(),
				coach.firstName(),
				coach.lastName(),
				coach.pesel(),
				coach.createdAt(),
				coach.modifiedAt(),
				coach.creator().firstName(),
				coach.creator().lastName(),
				Optional.ofNullable(coach.modifier()).map(InnerUserDto::firstName).orElse(null),
				Optional.ofNullable(coach.modifier()).map(InnerUserDto::lastName).orElse(null)
		);
	}
}
