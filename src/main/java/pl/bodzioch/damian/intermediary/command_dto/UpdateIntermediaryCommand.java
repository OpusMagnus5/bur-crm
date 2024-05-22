package pl.bodzioch.damian.intermediary.command_dto;

import pl.bodzioch.damian.dto.UpdateIntermediaryRequest;
import pl.bodzioch.damian.infrastructure.command.Command;
import pl.bodzioch.damian.utils.CipherComponent;

public record UpdateIntermediaryCommand(
		Long id,
		Integer version,
		String name,
		Long nip,
		Long modifiedBy //TODO poprawic
) implements Command<UpdateIntermediaryCommandResult> {

	public UpdateIntermediaryCommand(UpdateIntermediaryRequest request, CipherComponent cipher) {
		this(
				Long.parseLong(cipher.decryptMessage(request.id())),
				request.version(),
				request.name(),
				Long.parseLong(request.nip()),
				1L
		);
	}
}
