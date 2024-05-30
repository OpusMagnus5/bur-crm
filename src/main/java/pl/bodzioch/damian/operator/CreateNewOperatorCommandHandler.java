package pl.bodzioch.damian.operator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.operator.command_dto.CreateNewOperatorCommand;
import pl.bodzioch.damian.operator.command_dto.CreateNewOperatorCommandResult;
import pl.bodzioch.damian.utils.MessageResolver;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
class CreateNewOperatorCommandHandler implements CommandHandler<CreateNewOperatorCommand, CreateNewOperatorCommandResult> {

    private final IOperatorWriteRepository writeRepository;
    private final MessageResolver messageResolver;

    @Override
    public Class<CreateNewOperatorCommand> commandClass() {
        return CreateNewOperatorCommand.class;
    }

    @Override
    @CacheEvict(value = "operators", allEntries = true)
    public CreateNewOperatorCommandResult handle(CreateNewOperatorCommand command) {
        Operator operator = new Operator(command);
        try {
            writeRepository.createNew(operator); //TODO dopisać sprawdzanie po lowercase name
        } catch (DuplicateKeyException e) {
            log.warn("Operator with name: {} already exists", command.name(), e);
            throw buildOperatorByNameAlreadyExistsException(command.name());
        }
        String message = messageResolver.getMessage("operator.createNewOperatorSuccess");
        return new CreateNewOperatorCommandResult(message);
    }

    private AppException buildOperatorByNameAlreadyExistsException(String name) {
        return new AppException(
                "Operator with name: " + name + " already exists!",
                HttpStatus.BAD_REQUEST,
                List.of(new ErrorData(
                        "error.client.operator.nameAlreadyExists",
                        List.of(name)
                ))
        );
    }
}
