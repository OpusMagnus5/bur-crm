package pl.bodzioch.damian.service_provider.command_dto;

import pl.bodzioch.damian.infrastructure.command.Command;

public record UpdateServiceProviderCommand(
        Long id,
        Integer version,
        String name,
        Long nip,
        Long modifiedBy //TODO poprawic
)
implements Command<UpdateServiceProviderCommandResult> {
}
