package pl.bodzioch.damian.dto;

import java.io.Serializable;
import java.util.List;

public record GetRolesResponse(

		List<RoleDto> roles

) implements Serializable {
}
