package pl.bodzioch.damian.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.customer.command_dto.UpdateCustomerCommand;
import pl.bodzioch.damian.customer.command_dto.UpdateCustomerCommandResult;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.utils.MessageResolver;

@Component
@RequiredArgsConstructor
class UpdateCustomerCommandHandler implements CommandHandler<UpdateCustomerCommand, UpdateCustomerCommandResult> {

	private final ICustomerWriteRepository writeRepository;
	private final MessageResolver messageResolver;

	@Override
	public Class<UpdateCustomerCommand> commandClass() {
		return UpdateCustomerCommand.class;
	}

	@Override
	@CacheEvict(value = "customers", allEntries = true)
	public UpdateCustomerCommandResult handle(UpdateCustomerCommand command) {
		writeRepository.update(new Customer(command));
		String message = messageResolver.getMessage("customer.updateSuccess");
		return new UpdateCustomerCommandResult(message);
	}
}
