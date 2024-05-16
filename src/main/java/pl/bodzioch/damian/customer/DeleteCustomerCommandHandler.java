package pl.bodzioch.damian.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.customer.command_dto.DeleteCustomerCommand;
import pl.bodzioch.damian.customer.command_dto.DeleteCustomerCommandResult;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.utils.MessageResolver;

@Component
@RequiredArgsConstructor
public class DeleteCustomerCommandHandler implements CommandHandler<DeleteCustomerCommand, DeleteCustomerCommandResult> {

    private final ICustomerWriteRepository writeRepository;
    private final MessageResolver messageResolver;

    @Override
    public Class<DeleteCustomerCommand> commandClass() {
        return DeleteCustomerCommand.class;
    }

    @Override
    public DeleteCustomerCommandResult handle(DeleteCustomerCommand command) {
        writeRepository.delete(command.id());
        String message = messageResolver.getMessage("customer.deleteByIdSuccess");
        return new DeleteCustomerCommandResult(message);
    }
}
