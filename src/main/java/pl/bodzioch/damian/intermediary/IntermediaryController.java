package pl.bodzioch.damian.intermediary;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.dto.*;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.intermediary.command_dto.*;
import pl.bodzioch.damian.intermediary.query_dto.*;
import pl.bodzioch.damian.utils.CipherComponent;

import java.util.HashMap;
import java.util.List;

import static pl.bodzioch.damian.intermediary.IntermediaryFilterField.NAME;
import static pl.bodzioch.damian.intermediary.IntermediaryFilterField.NIP;

@RestController
@RequestMapping("/api/intermediary")
@RequiredArgsConstructor
@Validated
class IntermediaryController {

    private final CommandExecutor commandExecutor;
    private final QueryExecutor queryExecutor;
    private final CipherComponent cipher;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateNewIntermediaryResponse createNew(@Valid @RequestBody CreateNewIntermediaryRequest request) {
        CreateNewIntermediaryCommand command = new CreateNewIntermediaryCommand(request);
        CreateNewIntermediaryCommandResult result = commandExecutor.execute(command);
        return new CreateNewIntermediaryResponse(result.message());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/exists", params = {"nip"})
    IntermediaryExistsResponse isProviderExists(@RequestParam String nip) {
        GetIntermediaryByNipQuery query = new GetIntermediaryByNipQuery(Long.parseLong(nip));
        try {
            queryExecutor.execute(query);
        } catch (AppException e) {
            return new IntermediaryExistsResponse(false);
        }
        return new IntermediaryExistsResponse(true);
    }

    @GetMapping(params = {"pageNumber", "pageSize"})
    @ResponseStatus(HttpStatus.OK)
    IntermediaryPageResponse getServiceProviders(
            @RequestParam
            @Min(value = 1, message = "error.client.minPageNumber")
            @Max(value = Integer.MAX_VALUE, message = "error.client.maxPageNumber")
            int pageNumber,
            @Min(value = 10, message = "error.client.minPageSize")
            @Max(value = 50, message = "error.client.maxPageSize")
            @RequestParam int pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String nip){
        HashMap<IntermediaryFilterField, Object> filters = new HashMap<>();
        filters.put(NAME, name);
        filters.put(NIP, NumberUtils.isParsable(nip) ? Long.parseLong(nip) : null);
        GetIntermediaryPageQuery query = new GetIntermediaryPageQuery(pageNumber, pageSize, filters);
        GetIntermediaryPageQueryResult result = queryExecutor.execute(query);
        List<IntermediaryData> intermediaryData = result.intermediaries().stream()
                .map(element -> new IntermediaryData(element, cipher))
                .toList();
        return new IntermediaryPageResponse(intermediaryData, result.totalIntermediaries());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    DeleteIntermediaryResponse delete(@PathVariable String id) {
        long intermediaryId = Long.parseLong(cipher.decryptMessage(id));
        DeleteIntermediaryCommand command = new DeleteIntermediaryCommand(intermediaryId);
        DeleteIntermediaryCommandResult result = commandExecutor.execute(command);
        return new DeleteIntermediaryResponse(result.message());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    IntermediaryDetailsResponse getDetails(@PathVariable String id) {
        long intermediaryId = Long.parseLong(cipher.decryptMessage(id));
        GetIntermediaryDetailsQuery query = new GetIntermediaryDetailsQuery(intermediaryId);
        GetIntermediaryDetailsQueryResult result = queryExecutor.execute(query);
        return new IntermediaryDetailsResponse(result.intermediary(), cipher);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    UpdateIntermediaryResponse update(@Valid @RequestBody UpdateIntermediaryRequest request) {
        UpdateIntermediaryCommand command = new UpdateIntermediaryCommand(request, cipher);
        UpdateIntermediaryCommandResult result = commandExecutor.execute(command);
        return new UpdateIntermediaryResponse(result.message());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    GetAllIntermediariesResponse getAll() {
        GetAllIntermediariesQuery query = new GetAllIntermediariesQuery();
        GetAllIntermediariesQueryResult result = queryExecutor.execute(query);
        List<IntermediaryData> intermediaries = result.intermediaries().stream()
                .map(intermediary -> new IntermediaryData(intermediary, cipher))
                .toList();
        return new GetAllIntermediariesResponse(intermediaries);
    }
}
