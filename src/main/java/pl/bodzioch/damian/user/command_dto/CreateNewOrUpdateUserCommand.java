package pl.bodzioch.damian.user.command_dto;

import pl.bodzioch.damian.dto.CreateNewOrUpdateUserRequest;
import pl.bodzioch.damian.infrastructure.command.Command;
import pl.bodzioch.damian.user.UserRole;
import pl.bodzioch.damian.utils.CipherComponent;

import java.util.Optional;

public record CreateNewOrUpdateUserCommand(
        Long id,
        Integer version,
        String email,
        String firstName,
        String lastName,
        Long creatorId,
        UserRole role

) implements Command<CreateNewOrUpdateUserCommandResult>{

    public CreateNewOrUpdateUserCommand(CreateNewOrUpdateUserRequest request, CipherComponent cipher) {
        this(
                Optional.ofNullable(request.id()).map(cipher::decryptMessage).map(Long::parseLong).orElse(null),
                request.version(),
                request.email(),
                request.firstName(),
                request.lastName(),
                cipher.getPrincipalId(),
                UserRole.valueOf(request.role())
        );
    }
}


