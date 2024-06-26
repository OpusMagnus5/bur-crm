package pl.bodzioch.damian.operator;

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
import pl.bodzioch.damian.operator.command_dto.*;
import pl.bodzioch.damian.operator.query_dto.*;
import pl.bodzioch.damian.utils.CipherComponent;

import java.util.HashMap;
import java.util.List;

import static pl.bodzioch.damian.operator.OperatorFilterField.NAME;

@RestController
@RequestMapping("/api/operator")
@Validated
@RequiredArgsConstructor
class OperatorController {

    private final CommandExecutor commandExecutor;
    private final QueryExecutor queryExecutor;
    private final CipherComponent cipher;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateNewOperatorResponse createNew(@Valid @RequestBody CreateNewOperatorRequest request) {
        CreateNewOperatorCommand command = new CreateNewOperatorCommand(request, cipher);
        CreateNewOperatorCommandResult result = commandExecutor.execute(command);
        return new CreateNewOperatorResponse(result.message());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/exists")
    OperatorExistsResponse isProviderExists(@RequestParam String name) {
        GetOperatorByNameQuery query = new GetOperatorByNameQuery(name);
        try {
            queryExecutor.execute(query);
        } catch (AppException e) {
            return new OperatorExistsResponse(false);
        }
        return new OperatorExistsResponse(true);
    }

    @GetMapping(params = {"pageNumber", "pageSize"})
    @ResponseStatus(HttpStatus.OK)
    OperatorsPageResponse getServiceProviders(
            @RequestParam
            @Min(value = 1, message = "error.client.minPageNumber")
            @Max(value = Integer.MAX_VALUE, message = "error.client.maxPageNumber")
            int pageNumber,
            @Min(value = 10, message = "error.client.minPageSize")
            @Max(value = 50, message = "error.client.maxPageSize")
            @RequestParam int pageSize,
            @RequestParam(required = false) String name){
        HashMap<OperatorFilterField, Object> filters = new HashMap<>();
        filters.put(NAME, name);
        GetOperatorsPageQuery query = new GetOperatorsPageQuery(pageNumber, pageSize, filters);
        GetOperatorsPageQueryResult result = queryExecutor.execute(query);
        List<OperatorData> operatorData = result.operators().stream()
                .map(element -> new OperatorData(element, cipher))
                .toList();
        return new OperatorsPageResponse(operatorData, result.totalOperators());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    DeleteOperatorResponse delete(@PathVariable String id) {
        long operatorId = Long.parseLong(cipher.decryptMessage(id));
        DeleteOperatorCommand command = new DeleteOperatorCommand(operatorId);
        DeleteOperatorCommandResult result = commandExecutor.execute(command);
        return new DeleteOperatorResponse(result.message());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    GetOperatorDetailsResponse getDetails(@PathVariable String id) {
        long operatorId = Long.parseLong(cipher.decryptMessage(id));
        GetOperatorDetailsQuery query = new GetOperatorDetailsQuery(operatorId);
        GetOperatorDetailsQueryResult result = queryExecutor.execute(query);
        return new GetOperatorDetailsResponse(result.operator(), cipher);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    UpdateOperatorResponse update(@Valid @RequestBody UpdateOperatorRequest request) {
        UpdateOperatorCommand command = new UpdateOperatorCommand(request, cipher);
        UpdateOperatorCommandResult result = commandExecutor.execute(command);
        return new UpdateOperatorResponse(result.message());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    AllOperatorsResponse getAll() {
        GetAllOperatorsQuery query = new GetAllOperatorsQuery();
        GetAllOperatorsQueryResult result = queryExecutor.execute(query);
        List<OperatorData> operators = result.operators().stream()
                .map(el -> new OperatorData(el, cipher))
                .toList();
        return new AllOperatorsResponse(operators);
    }
}
