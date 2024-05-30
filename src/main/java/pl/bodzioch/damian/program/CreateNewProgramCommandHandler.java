package pl.bodzioch.damian.program;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.program.command_dto.CreateNewProgramCommand;
import pl.bodzioch.damian.program.command_dto.CreateNewProgramCommandResult;
import pl.bodzioch.damian.utils.MessageResolver;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
class CreateNewProgramCommandHandler implements CommandHandler<CreateNewProgramCommand, CreateNewProgramCommandResult> {

    private final IProgramWriteRepository writeRepository;
    private final MessageResolver messageResolver;

    @Override
    public Class<CreateNewProgramCommand> commandClass() {
        return CreateNewProgramCommand.class;
    }

    @Override
    @CacheEvict(value = "programs", allEntries = true)
    public CreateNewProgramCommandResult handle(CreateNewProgramCommand command) {
        Program program = new Program(command);
        try {
            writeRepository.createNew(program);
        } catch (DuplicateKeyException e) {
            log.warn("Program with name: {} already exists", command.name(), e);
            throw buildProgramByNameAlreadyExistsException(command.name());
        }
        String message = messageResolver.getMessage("program.createNewProgramSuccess");
        return new CreateNewProgramCommandResult(message);
    }

    private AppException buildProgramByNameAlreadyExistsException(String name) {
        return new AppException(
                "Program with name: " + name + " already exists!",
                HttpStatus.BAD_REQUEST,
                List.of(new ErrorData(
                        "error.client.operator.nameAlreadyExists",
                        List.of(name)
                ))
        );
    }
}
