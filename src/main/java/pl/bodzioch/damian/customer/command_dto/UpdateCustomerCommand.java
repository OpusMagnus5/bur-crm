package pl.bodzioch.damian.customer.command_dto;

import pl.bodzioch.damian.dto.UpdateCustomerRequest;
import pl.bodzioch.damian.infrastructure.command.Command;
import pl.bodzioch.damian.utils.CipherComponent;

public record UpdateCustomerCommand(
		Long id,
		Integer version,
		String name,
		Long nip,
		Long modifiedBy
) implements Command<UpdateCustomerCommandResult> {

	public UpdateCustomerCommand(UpdateCustomerRequest request, CipherComponent cipher) {
		this(
				Long.parseLong(cipher.decryptMessage(request.id())),
				request.version(),
				request.name(),
				Long.parseLong(request.nip()),
				cipher.getPrincipalId()
		);
	}
}
