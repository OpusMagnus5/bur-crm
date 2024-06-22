package pl.bodzioch.damian.program.command_dto;

import pl.bodzioch.damian.dto.UpdateProgramRequest;
import pl.bodzioch.damian.infrastructure.command.Command;
import pl.bodzioch.damian.utils.CipherComponent;

public record UpdateProgramCommand(
		Long id,
		Integer version,
		String name,
		Long operatorId,
		Long modifiedBy
) implements Command<UpdateProgramCommandResult> {

	public UpdateProgramCommand(UpdateProgramRequest request, CipherComponent cipher) {
		this(
				Long.parseLong(cipher.decryptMessage(request.id())),
				request.version(),
				request.name(),
				Long.parseLong(cipher.decryptMessage(request.operatorId())),
				cipher.getPrincipalId()
		);
	}
}
