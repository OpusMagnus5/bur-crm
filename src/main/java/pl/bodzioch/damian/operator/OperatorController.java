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
import pl.bodzioch.damian.operator.command_dto.CreateNewOperatorCommand;
import pl.bodzioch.damian.operator.command_dto.CreateNewOperatorCommandResult;
import pl.bodzioch.damian.operator.query_dto.GetOperatorByNameQuery;
import pl.bodzioch.damian.operator.query_dto.GetOperatorsPageQuery;
import pl.bodzioch.damian.operator.query_dto.GetOperatorsPageQueryResult;
import pl.bodzioch.damian.utils.CipherComponent;
import pl.bodzioch.damian.utils.validator.OperatorIdKindV;

import java.util.List;

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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    OperatorsPageResponse getServiceProviders(
            @RequestParam
            @Min(value = 1, message = "error.client.minPageNumber")
            @Max(value = Integer.MAX_VALUE, message = "error.client.maxPageNumber")
            int pageNumber,
            @Min(value = 10, message = "error.client.minPageSize")
            @Max(value = 50, message = "error.client.maxPageSize")
            @RequestParam int pageSize){
        GetOperatorsPageQuery query = new GetOperatorsPageQuery(pageNumber, pageSize);
        GetOperatorsPageQueryResult result = queryExecutor.execute(query);
        List<OperatorData> providersData = result.operators().stream()
                .map(element -> new OperatorData(element, cipher))
                .toList();
        return new OperatorsPageResponse(providersData, result.totalOperators());
    }
}
