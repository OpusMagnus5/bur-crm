package pl.bodzioch.damian.coach;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.coach.command_dto.CreateNewCoachCommand;
import pl.bodzioch.damian.coach.command_dto.CreateNewCoachCommandResult;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.utils.MessageResolver;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Cacheable("coaches")
@Slf4j
@Component
@RequiredArgsConstructor
class CreateNewCoachCommandHandler implements CommandHandler<CreateNewCoachCommand, CreateNewCoachCommandResult> {

    private final ICoachWriteRepository writeRepository;
    private final MessageResolver messageResolver;

    @Override
    public Class<CreateNewCoachCommand> commandClass() {
        return CreateNewCoachCommand.class;
    }

    @Override
    public CreateNewCoachCommandResult handle(CreateNewCoachCommand command) {
        try {
            Coach coach = new Coach(command);
            writeRepository.createNew(coach);
        } catch (DuplicateKeyException e) {
            log.warn("Coach with PESEL: {} already exists", command.pesel(), e);
            throw buildCoachByPeselAlreadyExistsException(command.pesel());
        }
        String message = messageResolver.getMessage("coach.createNewCoachSuccess");
        return new CreateNewCoachCommandResult(message);
    }

    private AppException buildCoachByPeselAlreadyExistsException(String pesel) {
        return new AppException(
                HttpStatus.BAD_REQUEST,
                List.of(new ErrorData(
                        "error.client.coach.peselAlreadyExists",
                        List.of(pesel)
                ))
        );
    }
}
