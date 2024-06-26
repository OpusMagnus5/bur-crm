package pl.bodzioch.damian.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.customer.command_dto.DeleteCustomerCommand;
import pl.bodzioch.damian.customer.command_dto.DeleteCustomerCommandResult;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.utils.MessageResolver;

@Component
@RequiredArgsConstructor
class DeleteCustomerCommandHandler implements CommandHandler<DeleteCustomerCommand, DeleteCustomerCommandResult> {

    private final ICustomerWriteRepository writeRepository;
    private final MessageResolver messageResolver;

    @Override
    public Class<DeleteCustomerCommand> commandClass() {
        return DeleteCustomerCommand.class;
    }

    @Override
    @CacheEvict(value = "customers", allEntries = true)
    public DeleteCustomerCommandResult handle(DeleteCustomerCommand command) {
        writeRepository.delete(command.id());
        String message = messageResolver.getMessage("customer.deleteByIdSuccess");
        return new DeleteCustomerCommandResult(message);
    }
}
