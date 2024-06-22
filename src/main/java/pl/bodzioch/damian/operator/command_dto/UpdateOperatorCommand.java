package pl.bodzioch.damian.operator.command_dto;

import pl.bodzioch.damian.dto.UpdateOperatorRequest;
import pl.bodzioch.damian.infrastructure.command.Command;
import pl.bodzioch.damian.utils.CipherComponent;

public record UpdateOperatorCommand(
		Long id,
		Integer version,
		String name,
		String notes,
		Long modifiedBy
) implements Command<UpdateOperatorCommandResult> {

	public UpdateOperatorCommand(UpdateOperatorRequest request, CipherComponent cipher) {
		this(
				cipher.getDecryptedId(request.id()),
				request.version(),
				request.name(),
				request.notes(),
				cipher.getPrincipalId()
		);
	}
}
