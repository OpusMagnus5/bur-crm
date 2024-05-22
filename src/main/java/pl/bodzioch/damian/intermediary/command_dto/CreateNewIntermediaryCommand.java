package pl.bodzioch.damian.intermediary.command_dto;

import pl.bodzioch.damian.dto.CreateNewIntermediaryRequest;
import pl.bodzioch.damian.infrastructure.command.Command;

public record CreateNewIntermediaryCommand(
        String name,
        Long nip,
        Long createdBy //TODO poprawic
) implements Command<CreateNewIntermediaryCommandResult> {

    public CreateNewIntermediaryCommand(CreateNewIntermediaryRequest request) {
        this(
                request.name(),
                Long.parseLong(request.nip()),
                1L
        );
    }

}
