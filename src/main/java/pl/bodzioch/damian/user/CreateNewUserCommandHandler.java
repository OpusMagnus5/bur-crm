package pl.bodzioch.damian.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.user.command_dto.CreateNewUserCommand;
import pl.bodzioch.damian.user.command_dto.CreateNewUserCommandResult;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
class CreateNewUserCommandHandler implements CommandHandler<CreateNewUserCommand, CreateNewUserCommandResult> {

    private final IUserWriteRepository writeRepository;

    @Override
    public Class<CreateNewUserCommand> commandClass() {
        return CreateNewUserCommand.class;
    }

    @Override
    @Transactional //TODO dopisać walidacje roli zakładającego konto
    public CreateNewUserCommandResult handle(CreateNewUserCommand command) {
        String firstPassword = User.generateFirstPassword();
        User user = new User(command, firstPassword);
        CreateNewUserCommandResult result = new CreateNewUserCommandResult(user.email(), firstPassword);
        try {
            writeRepository.createNew(user);
        } catch (DuplicateKeyException e) {
            log.warn("User with email: {} already exists", command.email(), e);
            throw buildUserByEmailAlreadyExistsException(command.email());
        }
        return result;
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
