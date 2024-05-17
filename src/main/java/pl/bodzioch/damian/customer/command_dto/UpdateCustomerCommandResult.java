package pl.bodzioch.damian.customer.command_dto;

import pl.bodzioch.damian.infrastructure.command.CommandResult;

public record UpdateCustomerCommandResult(
		String message
) implements CommandResult {
}
