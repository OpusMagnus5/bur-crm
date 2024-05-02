package pl.bodzioch.damian.service_provider.command_dto;

import pl.bodzioch.damian.infrastructure.command.Command;

public record DeleteServiceProviderCommand(
        Long id
) implements Command<DeleteServiceProviderCommandResult> {
}
