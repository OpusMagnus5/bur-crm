package pl.bodzioch.damian.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.dto.*;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.user.commandDto.CreateNewUserCommand;
import pl.bodzioch.damian.user.commandDto.CreateNewUserCommandResult;
import pl.bodzioch.damian.user.commandDto.GetAllRolesCommand;
import pl.bodzioch.damian.user.commandDto.GetAllRolesCommandResult;
import pl.bodzioch.damian.user.queryDto.CheckUserExistenceQuerResult;
import pl.bodzioch.damian.user.queryDto.CheckUserExistenceQuery;
import pl.bodzioch.damian.user.queryDto.GetUsersPageQuery;
import pl.bodzioch.damian.user.queryDto.GetUsersPageQueryResult;
import pl.bodzioch.damian.utils.CipherComponent;
import pl.bodzioch.damian.utils.validator.IdKindV;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
class UserController {

    private final CommandExecutor commandExecutor;
    private final QueryExecutor queryExecutor;
    private final CipherComponent cipher;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    CreateUserResponse createNewUser(@Valid @RequestBody CreateNewUserRequest request) {
        CreateNewUserCommand command = new CreateNewUserCommand(
                request.email(), request.firstName(), request.lastName(), 1L, request.role()
        );
        CreateNewUserCommandResult result = commandExecutor.execute(command);
        return new CreateUserResponse(result.login(), result.password());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    void login() {

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/allRoles")
    GetRolesResponse getRoles() {
        GetAllRolesCommand command = new GetAllRolesCommand();
        GetAllRolesCommandResult result = commandExecutor.execute(command);
        return new GetRolesResponse(result.roles());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/exists")
    UserExistsResponse isUserExists(@RequestParam @IdKindV(message = "error.client.incorrectIdKind") String kindOfId, @RequestParam String id) {
        CheckUserExistenceQuery query = new CheckUserExistenceQuery(IdKind.valueOf(kindOfId), id);
        CheckUserExistenceQuerResult result = queryExecutor.execute(query);
        return new UserExistsResponse(result.exists());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    UserPageResponse getUsers(
            @RequestParam
            @Min(value = 1, message = "error.client.minPageNumber")
            @Max(value = Integer.MAX_VALUE, message = "error.client.maxPageNumber")
            int pageNumber,
            @Min(value = 10, message = "error.client.minPageSize")
            @Max(value = 50, message = "error.client.maxPageSize")
            @RequestParam int pageSize) {
        GetUsersPageQuery query = new GetUsersPageQuery(pageNumber, pageSize);
        GetUsersPageQueryResult result = queryExecutor.execute(query);
        List<UserListData> users = result.users().stream()
                .map(userDto -> new UserListData(userDto, cipher))
                .toList();
        return new UserPageResponse(users, result.totalUsers());
    }
}
