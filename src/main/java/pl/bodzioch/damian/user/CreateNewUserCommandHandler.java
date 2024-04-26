package pl.bodzioch.damian.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.user.commandDto.CreateNewUserCommand;
import pl.bodzioch.damian.user.commandDto.CreateNewUserCommandResult;
import pl.bodzioch.damian.valueobject.ErrorData;

import java.util.List;

@Component
@RequiredArgsConstructor
class CreateNewUserCommandHandler implements CommandHandler<CreateNewUserCommand, CreateNewUserCommandResult> {

    private final IUserReadRepository readRepository;
    private final IUserWriteRepository writeRepository;

    @Override
    public Class<CreateNewUserCommand> commandClass() {
        return CreateNewUserCommand.class;
    }

    @Override
    @Transactional //TODO dopisać walidacje roli zakładającego konto
    public CreateNewUserCommandResult handle(CreateNewUserCommand command) {
        boolean isUserExists = readRepository.getByEmail(command.email()).isPresent();
        if (isUserExists) {
            throw buildUserByEmailAlreadyExistsException(command.email());
        }
        User user = new User(command);
        CreateNewUserCommandResult result = new CreateNewUserCommandResult(user.email(), user.password());
        writeRepository.createNew(new UserEntityWrite(user));
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
