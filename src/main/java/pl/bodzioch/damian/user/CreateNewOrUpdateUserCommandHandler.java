package pl.bodzioch.damian.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.user.command_dto.CreateNewOrUpdateUserCommand;
import pl.bodzioch.damian.user.command_dto.CreateNewOrUpdateUserCommandResult;
import pl.bodzioch.damian.utils.MessageResolver;
import pl.bodzioch.damian.utils.PermissionService;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
class CreateNewOrUpdateUserCommandHandler implements CommandHandler<CreateNewOrUpdateUserCommand, CreateNewOrUpdateUserCommandResult> {

    private final IUserWriteRepository writeRepository;
    private final MessageResolver messageResolver;
    private final PermissionService permissionService;

    @Override
    public Class<CreateNewOrUpdateUserCommand> commandClass() {
        return CreateNewOrUpdateUserCommand.class;
    }

    @Override
    @Transactional
    public CreateNewOrUpdateUserCommandResult handle(CreateNewOrUpdateUserCommand command) {
        List<UserRole> creatorRoles = permissionService.getRoles();
        String firstPassword = User.generateNewPassword();
        User user = new User(command, firstPassword, creatorRoles);
        try {
            writeRepository.createNew(user);
        } catch (DuplicateKeyException e) {
            log.warn("User with email: {} already exists", command.email(), e);
            throw buildUserByEmailAlreadyExistsException(command.email());
        }

        String message = null;
        if (user.id() != null) {
            message = messageResolver.getMessage("user.updateSuccess");
        }
        return new CreateNewOrUpdateUserCommandResult(user.email(), firstPassword, message);
    }

    private AppException buildUserByEmailAlreadyExistsException(String email) {
        return new AppException(
                HttpStatus.BAD_REQUEST,
                List.of(new ErrorData(
                        "error.client.userByEmailAlreadyExists",
                        List.of(email)
                ))
        );
    }
}
