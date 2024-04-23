package pl.bodzioch.damian.user.commandDto;

import pl.bodzioch.damian.dto.RoleDto;
import pl.bodzioch.damian.infrastructure.command.CommandResult;

import java.util.List;

public record GetAllRolesCommandResult(

		List<RoleDto> roles

) implements CommandResult {
}
