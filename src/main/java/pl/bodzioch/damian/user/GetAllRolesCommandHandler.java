package pl.bodzioch.damian.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.dto.RoleDto;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.user.command_dto.GetAllRolesCommand;
import pl.bodzioch.damian.user.command_dto.GetAllRolesCommandResult;
import pl.bodzioch.damian.utils.MessageResolver;

import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
class GetAllRolesCommandHandler implements CommandHandler<GetAllRolesCommand, GetAllRolesCommandResult> {

	private final MessageResolver messageResolver;


	@Override
	public Class<GetAllRolesCommand> commandClass() {
		return GetAllRolesCommand.class;
	}

	@Override
	public GetAllRolesCommandResult handle(GetAllRolesCommand command) {
		List<RoleDto> rolesList = Arrays.stream(UserRole.values())
				.map(Enum::name)
				.map(role -> new RoleDto(
						role,
						messageResolver.getMessage("user.role." + role)
				))
				.toList();
		return new GetAllRolesCommandResult(rolesList);
	}
}
