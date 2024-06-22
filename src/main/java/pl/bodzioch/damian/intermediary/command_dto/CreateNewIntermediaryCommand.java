package pl.bodzioch.damian.intermediary.command_dto;

import pl.bodzioch.damian.dto.CreateNewIntermediaryRequest;
import pl.bodzioch.damian.infrastructure.command.Command;
import pl.bodzioch.damian.utils.CipherComponent;

public record CreateNewIntermediaryCommand(
        String name,
        Long nip,
        Long createdBy
) implements Command<CreateNewIntermediaryCommandResult> {

    public CreateNewIntermediaryCommand(CreateNewIntermediaryRequest request, CipherComponent cipher) {
        this(
                request.name(),
                Long.parseLong(request.nip()),
                cipher.getPrincipalId()
        );
    }

}
