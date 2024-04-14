package pl.bodzioch.damian.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.configuration.security.UserDetailsDto;
import pl.bodzioch.damian.dto.CreateNewUserRequest;
import pl.bodzioch.damian.dto.CreateUserResponse;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
class UserController {

    private final CommandExecutor commandExecutor;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    CreateUserResponse createNewUser(@Valid @RequestBody CreateNewUserRequest request, @AuthenticationPrincipal UserDetailsDto user) {
        CreateNewUserCommand command = new CreateNewUserCommand(request.email(), request.firstName(), request.lastName(), user.getId());
        CreateNewUserCommandResult result = commandExecutor.execute(command);
        return new CreateUserResponse(result.login(), result.password(), result.message());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    void login() {

    }
}
