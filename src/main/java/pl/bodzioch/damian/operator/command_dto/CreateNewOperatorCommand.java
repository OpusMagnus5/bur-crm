package pl.bodzioch.damian.operator.command_dto;

import pl.bodzioch.damian.infrastructure.command.Command;

public record CreateNewOperatorCommand(
        String name,
        String phoneNumber,
        Long createdBy

) implements Command<CreateNewOperatorCommandResult> {
}
