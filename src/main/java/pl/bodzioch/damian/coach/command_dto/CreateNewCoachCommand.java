package pl.bodzioch.damian.coach.command_dto;

import pl.bodzioch.damian.dto.CreateNewCoachRequest;
import pl.bodzioch.damian.infrastructure.command.Command;

public record CreateNewCoachCommand(
        String firstName,
        String lastName,
        String pesel,
        Long createdBy
) implements Command<CreateNewCoachCommandResult> {

    public CreateNewCoachCommand(CreateNewCoachRequest request) {
        this(
                request.firstName(),
                request.lastName(),
                request.pesel(),
                1L //TODO poprawić
        );
    }
}
