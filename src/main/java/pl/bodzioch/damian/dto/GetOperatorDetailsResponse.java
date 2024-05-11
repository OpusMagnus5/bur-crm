package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.operator.OperatorDto;
import pl.bodzioch.damian.user.InnerUserDto;
import pl.bodzioch.damian.utils.CipherComponent;

import java.time.LocalDateTime;
import java.util.List;

public record GetOperatorDetailsResponse(
		String id,
		Integer version,
		String name,
		String notes,
		LocalDateTime createdAt,
		LocalDateTime modifiedAt,
		String creatorFirstName,
		String creatorLastName,
		String modifierFirstName,
		String modifierLastName,
		List<ProgramData> programs
) {

	public GetOperatorDetailsResponse(OperatorDto operator, CipherComponent cipher) {
		this(
				cipher.encryptMessage(operator.id().toString()),
				operator.version(),
				operator.name(),
				operator.notes(),
				operator.createdAt(),
				operator.modifiedAt(),
				operator.getCreator().map(InnerUserDto::firstName).orElse(null),
				operator.getCreator().map(InnerUserDto::lastName).orElse(null),
				operator.getModifier().map(InnerUserDto::firstName).orElse(null),
				operator.getModifier().map(InnerUserDto::lastName).orElse(null),
				operator.programs().stream()
						.map(ProgramData::new)
						.toList()
		);
	}
}
