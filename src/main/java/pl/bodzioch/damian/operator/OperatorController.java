package pl.bodzioch.damian.operator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.dto.CreateNewOperatorRequest;
import pl.bodzioch.damian.dto.CreateNewOperatorResponse;
import pl.bodzioch.damian.dto.OperatorExistsResponse;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.operator.command_dto.CreateNewOperatorCommand;
import pl.bodzioch.damian.operator.command_dto.CreateNewOperatorCommandResult;
import pl.bodzioch.damian.operator.query_dto.GetOperatorByNameQuery;
import pl.bodzioch.damian.utils.validator.OperatorIdKindV;

@RestController
@RequestMapping("/api/operator")
@Validated
@RequiredArgsConstructor
class OperatorController {

    private final CommandExecutor commandExecutor;
    private final QueryExecutor queryExecutor;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateNewOperatorResponse createNew(@Valid @RequestBody CreateNewOperatorRequest request) {
        CreateNewOperatorCommand command = new CreateNewOperatorCommand(request.name(), request.phoneNumber(), 1L);//TODO poprawić
        CreateNewOperatorCommandResult result = commandExecutor.execute(command);
        return new CreateNewOperatorResponse(result.message());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/exists")
    OperatorExistsResponse isProviderExists(
            @RequestParam @OperatorIdKindV String kindOfId,
            @RequestParam String id) {
        GetOperatorByNameQuery query = new GetOperatorByNameQuery(id);
        try {
            queryExecutor.execute(query);
        } catch (AppException e) {
            return new OperatorExistsResponse(false);
        }
        return new OperatorExistsResponse(true);
    }
}
