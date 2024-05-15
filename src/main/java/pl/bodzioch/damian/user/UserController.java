package pl.bodzioch.damian.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.dto.*;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.user.command_dto.*;
import pl.bodzioch.damian.user.query_dto.*;
import pl.bodzioch.damian.utils.CipherComponent;

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
        CreateNewUserCommand command = new CreateNewUserCommand( //TODO dopisać creatora
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
    UserExistsResponse isUserExists(@RequestParam String email) {
        FindUserByEmailQuery query = new FindUserByEmailQuery(email);
        try {
            queryExecutor.execute(query);
            return new UserExistsResponse(true);
        } catch (AppException e) {
            return new UserExistsResponse(false);
        }
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

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    GetUserByIdResponse getUserById(@PathVariable String id) {
        long userId = Long.parseLong(cipher.decryptMessage(id));
        GetUserByIdQuery query = new GetUserByIdQuery(userId);
        GetUserByIdQueryResult result = queryExecutor.execute(query);
        return new GetUserByIdResponse(result.userDto(), cipher);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    DeleteUserByIdResponse deleteById(@PathVariable String id) {
        long userId = Long.parseLong(cipher.decryptMessage(id));
        DeleteUserByIdCommand command = new DeleteUserByIdCommand(userId);
        DeleteUserByIdCommandResult result = commandExecutor.execute(command);
        return new DeleteUserByIdResponse(result.message());
    }
}
