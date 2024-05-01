package pl.bodzioch.damian.service_provider.command_dto;

import pl.bodzioch.damian.infrastructure.command.CommandResult;

public record GetProviderNameByNipFromBurCommandResult(
        String name
) implements CommandResult {
}
