package pl.bodzioch.damian.program.command_dto;

import pl.bodzioch.damian.dto.CreateNewProgramRequest;
import pl.bodzioch.damian.infrastructure.command.Command;
import pl.bodzioch.damian.utils.CipherComponent;

public record CreateNewProgramCommand(
        Long operatorId,
        String name,
        Long createdBy
) implements Command<CreateNewProgramCommandResult> {

    public CreateNewProgramCommand(CreateNewProgramRequest request, CipherComponent cipher) {
        this(
                Long.parseLong(cipher.decryptMessage(request.operatorId())),
                request.name(),
                1L
        );
    }
}
