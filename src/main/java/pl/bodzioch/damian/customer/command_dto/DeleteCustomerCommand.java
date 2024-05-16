package pl.bodzioch.damian.customer.command_dto;

import pl.bodzioch.damian.infrastructure.command.Command;

public record DeleteCustomerCommand(
        Long id
) implements Command<DeleteCustomerCommandResult> {
}
