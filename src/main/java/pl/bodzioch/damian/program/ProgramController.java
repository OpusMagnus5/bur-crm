package pl.bodzioch.damian.program;

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
import pl.bodzioch.damian.program.command_dto.CreateNewProgramCommand;
import pl.bodzioch.damian.program.command_dto.CreateNewProgramCommandResult;
import pl.bodzioch.damian.program.query_dto.GetProgramByNameQuery;
import pl.bodzioch.damian.program.query_dto.GetProgramPageQuery;
import pl.bodzioch.damian.program.query_dto.GetProgramPageQueryResult;
import pl.bodzioch.damian.utils.CipherComponent;
import pl.bodzioch.damian.utils.validator.OperatorIdKindV;

import java.util.HashMap;
import java.util.List;

import static pl.bodzioch.damian.program.ProgramFilterField.NAME;

@RestController
@RequestMapping("/api/program")
@Validated
@RequiredArgsConstructor
class ProgramController {

    private final CommandExecutor commandExecutor;
    private final QueryExecutor queryExecutor;
    private final CipherComponent cipher;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateNewProgramResponse createNew(@Valid @RequestBody CreateNewProgramRequest request) {
        CreateNewProgramCommand command = new CreateNewProgramCommand(request, cipher);//TODO poprawić
        CreateNewProgramCommandResult result = commandExecutor.execute(command);
        return new CreateNewProgramResponse(result.message());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ProgramPageResponse getServiceProviders(
            @RequestParam
            @Min(value = 1, message = "error.client.minPageNumber")
            @Max(value = Integer.MAX_VALUE, message = "error.client.maxPageNumber")
            int pageNumber,
            @Min(value = 10, message = "error.client.minPageSize")
            @Max(value = 50, message = "error.client.maxPageSize")
            @RequestParam int pageSize,
            @RequestParam(required = false) String name){
        HashMap<ProgramFilterField, Object> filters = new HashMap<>();
        filters.put(NAME, name);
        GetProgramPageQuery query = new GetProgramPageQuery(pageNumber, pageSize, filters);
        GetProgramPageQueryResult result = queryExecutor.execute(query);
        List<ProgramData> programData = result.programs().stream()
                .map(element -> new ProgramData(element, cipher))
                .toList();
        return new ProgramPageResponse(programData, result.totalPrograms());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/exists")
    ProgramExistsResponse isProviderExists(
            @RequestParam @OperatorIdKindV String kindOfId,
            @RequestParam String id) {
        GetProgramByNameQuery query = new GetProgramByNameQuery(id);
        try {
            queryExecutor.execute(query);
        } catch (AppException e) {
            return new ProgramExistsResponse(false);
        }
        return new ProgramExistsResponse(true);
    }
}
