package pl.bodzioch.damian.operator.command_dto;

import pl.bodzioch.damian.dto.CreateNewOperatorRequest;
import pl.bodzioch.damian.infrastructure.command.Command;
import pl.bodzioch.damian.utils.CipherComponent;

public record CreateNewOperatorCommand(
        String name,
        String notes,
        Long createdBy

) implements Command<CreateNewOperatorCommandResult> {

    public CreateNewOperatorCommand(CreateNewOperatorRequest request, CipherComponent cipher) {
        this(request.name(), request.notes(), cipher.getPrincipalId());
    }
}
