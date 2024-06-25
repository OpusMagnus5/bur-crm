package pl.bodzioch.damian.user.command_dto;

import pl.bodzioch.damian.dto.ChangeUserPasswordRequest;
import pl.bodzioch.damian.infrastructure.command.Command;
import pl.bodzioch.damian.utils.CipherComponent;

public record ChangeUserPasswordCommand(
        Long userId,
        Integer version,
        String password
) implements Command<ChangeUserPasswordCommandResult> {

    public ChangeUserPasswordCommand(ChangeUserPasswordRequest request, CipherComponent cipher) {
        this(cipher.getDecryptedId(request.userId()), request.version(), request.password());
    }
}
