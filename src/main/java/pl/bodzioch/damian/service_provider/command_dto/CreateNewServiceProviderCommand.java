package pl.bodzioch.damian.service_provider.command_dto;

import pl.bodzioch.damian.infrastructure.command.Command;

public record CreateNewServiceProviderCommand(

        String name,
        Long nip,
        Long createdBy //TODO poprawic

) implements Command<CreateNewServiceProviderCommandResult> {
}
