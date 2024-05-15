package pl.bodzioch.damian.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.customer.command_dto.CreateNewCustomerCommand;
import pl.bodzioch.damian.customer.command_dto.CreateNewCustomerCommandResult;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.utils.MessageResolver;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateNewCustomerCommandHandler implements CommandHandler<CreateNewCustomerCommand, CreateNewCustomerCommandResult> {

    private final ICustomerWriteRepository writeRepository;
    private final MessageResolver messageResolver;

    @Override
    public Class<CreateNewCustomerCommand> commandClass() {
        return CreateNewCustomerCommand.class;
    }

    @Override
    public CreateNewCustomerCommandResult handle(CreateNewCustomerCommand command) {
        try {
            Customer customer = new Customer(command);
            writeRepository.createNew(customer);
        } catch (DuplicateKeyException e) {
            log.warn("Customer with NIP: {} already exists", command.nip(), e);
            throw buildCustomerByNipAlreadyExistsException(command.nip());
        }
        String message = messageResolver.getMessage("customer.createNewCustomerSuccess");
        return new CreateNewCustomerCommandResult(message);
    }

    private AppException buildCustomerByNipAlreadyExistsException(Long nip) {
        return new AppException(
                HttpStatus.BAD_REQUEST,
                List.of(new ErrorData(
                        "error.client.customer.nipAlreadyExists",
                        List.of(nip.toString())
                ))
        );
    }
}
