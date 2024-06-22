package pl.bodzioch.damian.service_provider.command_dto;

import pl.bodzioch.damian.dto.CreateNewServiceProviderRequest;
import pl.bodzioch.damian.infrastructure.command.Command;
import pl.bodzioch.damian.utils.CipherComponent;

public record CreateNewServiceProviderCommand(

        String name,
        Long nip,
        Long createdBy

) implements Command<CreateNewServiceProviderCommandResult> {

    public CreateNewServiceProviderCommand(CreateNewServiceProviderRequest request, CipherComponent cipher) {
        this(request.name(), Long.parseLong(request.nip()), cipher.getPrincipalId());
    }
}
