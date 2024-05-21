package pl.bodzioch.damian.coach.command_dto;

import pl.bodzioch.damian.dto.UpdateCoachRequest;
import pl.bodzioch.damian.infrastructure.command.Command;
import pl.bodzioch.damian.utils.CipherComponent;

public record UpdateCoachCommand(
        Long id,
        Integer version,
        String firstName,
        String lastName,
        String pesel,
        Long modifiedBy //TODO poprawic
) implements Command<UpdateCoachCommandResult> {

    public UpdateCoachCommand(UpdateCoachRequest request, CipherComponent cipher) {
        this(
                Long.parseLong(cipher.decryptMessage(request.id())),
                request.version(),
                request.firstName(),
                request.lastName(),
                request.pesel(),
                1L
        );
    }
}
