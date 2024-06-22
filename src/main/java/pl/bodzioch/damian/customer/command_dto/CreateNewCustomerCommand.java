package pl.bodzioch.damian.customer.command_dto;

import pl.bodzioch.damian.dto.CreateNewCustomerRequest;
import pl.bodzioch.damian.infrastructure.command.Command;
import pl.bodzioch.damian.utils.CipherComponent;

public record CreateNewCustomerCommand(
        String name,
        Long nip,
        Long createdBy
) implements Command<CreateNewCustomerCommandResult> {

    public CreateNewCustomerCommand(CreateNewCustomerRequest request, CipherComponent cipher) {
        this(
                request.name(),
                Long.parseLong(request.nip()),
                cipher.getPrincipalId()
        );
    }

}
