package pl.bodzioch.damian.customer.command_dto;

import pl.bodzioch.damian.infrastructure.command.Command;

public record CreateNewCustomerCommand(
        String name,
        Long nip,
        Long createdBy //TODO poprawic
) implements Command<CreateNewCustomerCommandResult> {

}