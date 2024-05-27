package pl.bodzioch.damian.intermediary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.intermediary.command_dto.CreateNewIntermediaryCommand;
import pl.bodzioch.damian.intermediary.command_dto.CreateNewIntermediaryCommandResult;
import pl.bodzioch.damian.utils.MessageResolver;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@CacheEvict(value = "intermediaries", allEntries = true)
@Slf4j
@Component
@RequiredArgsConstructor
class CreateNewIntermediaryCommandHandler implements CommandHandler<CreateNewIntermediaryCommand, CreateNewIntermediaryCommandResult> {

    private final IIntermediaryWriteRepository writeRepository;
    private final MessageResolver messageResolver;

    @Override
    public Class<CreateNewIntermediaryCommand> commandClass() {
        return CreateNewIntermediaryCommand.class;
    }

    @Override
    public CreateNewIntermediaryCommandResult handle(CreateNewIntermediaryCommand command) {
        try {
            Intermediary intermediary = new Intermediary(command);
            writeRepository.createNew(intermediary);
        } catch (DuplicateKeyException e) {
            log.warn("Intermediary with NIP: {} already exists", command.nip(), e);
            throw buildIntermediaryByNipAlreadyExistsException(command.nip());
        }
        String message = messageResolver.getMessage("intermediary.createNewIntermediarySuccess");
        return new CreateNewIntermediaryCommandResult(message);
    }

    private AppException buildIntermediaryByNipAlreadyExistsException(Long nip) {
        return new AppException(
                HttpStatus.BAD_REQUEST,
                List.of(new ErrorData(
                        "error.client.intermediary.nipAlreadyExists",
                        List.of(nip.toString())
                ))
        );
    }
}
