package pl.bodzioch.damian.service_provider.command_dto;

import pl.bodzioch.damian.dto.UpdateServiceProviderRequest;
import pl.bodzioch.damian.infrastructure.command.Command;
import pl.bodzioch.damian.utils.CipherComponent;

public record UpdateServiceProviderCommand(
        Long id,
        Integer version,
        String name,
        Long nip,
        Long modifiedBy
)
implements Command<UpdateServiceProviderCommandResult> {

    public UpdateServiceProviderCommand(UpdateServiceProviderRequest request, CipherComponent cipher) {
        this(cipher.getDecryptedId(request.id()), request.version(), request.name(), Long.parseLong(request.nip()), cipher.getPrincipalId());
    }
}
