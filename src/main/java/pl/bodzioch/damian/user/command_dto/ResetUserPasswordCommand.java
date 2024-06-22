package pl.bodzioch.damian.user.command_dto;

import pl.bodzioch.damian.dto.ResetUserPasswordRequest;
import pl.bodzioch.damian.infrastructure.command.Command;
import pl.bodzioch.damian.utils.CipherComponent;

public record ResetUserPasswordCommand(
        Long id,
        Integer userVersion,
        Long modifierId
) implements Command<ResetUserPasswordCommandResult> {

    public ResetUserPasswordCommand(ResetUserPasswordRequest request, CipherComponent cipher) {
        this(
                Long.parseLong(cipher.decryptMessage(request.id())),
                request.userVersion(),
                cipher.getPrincipalId()
        );
    }
}
