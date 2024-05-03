package pl.bodzioch.damian.operator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.dto.CreateNewOperatorRequest;
import pl.bodzioch.damian.dto.CreateNewOperatorResponse;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.operator.command_dto.CreateNewOperatorCommand;
import pl.bodzioch.damian.operator.command_dto.CreateNewOperatorCommandResult;

@RestController
@RequestMapping("/api/operator")
@Validated
@RequiredArgsConstructor
class OperatorController {

    private final CommandExecutor commandExecutor;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateNewOperatorResponse createNew(@Valid @RequestBody CreateNewOperatorRequest request) {
        CreateNewOperatorCommand command = new CreateNewOperatorCommand(request.name(), request.phoneNumber(), 1L);//TODO poprawić
        CreateNewOperatorCommandResult result = commandExecutor.execute(command);
        return new CreateNewOperatorResponse(result.message());
    }
}
