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
import pl.bodzioch.damian.program.command_dto.*;
import pl.bodzioch.damian.program.query_dto.*;
import pl.bodzioch.damian.utils.CipherComponent;

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
        CreateNewProgramCommand command = new CreateNewProgramCommand(request, cipher);
        CreateNewProgramCommandResult result = commandExecutor.execute(command);
        return new CreateNewProgramResponse(result.message());
    }

    @GetMapping(params = { "pageNumber", "pageSize" })
    @ResponseStatus(HttpStatus.OK)
    ProgramPageResponse getPrograms(
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
    ProgramExistsResponse isProgramExists(@RequestParam String name, @RequestParam String operatorId) {
        long operator = Long.parseLong(cipher.decryptMessage(operatorId));
        GetProgramByNameQuery query = new GetProgramByNameQuery(name, operator);
        try {
            queryExecutor.execute(query);
        } catch (AppException e) {
            return new ProgramExistsResponse(false);
        }
        return new ProgramExistsResponse(true);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    DeleteProgramResponse delete(@PathVariable String id) {
        long programId = Long.parseLong(cipher.decryptMessage(id));
        DeleteProgramCommand command = new DeleteProgramCommand(programId);
        DeleteProgramCommandResult result = commandExecutor.execute(command);
        return new DeleteProgramResponse(result.message());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    GetProgramDetailsResponse getDetails(@PathVariable String id) {
        long programId = Long.parseLong(cipher.decryptMessage(id));
        GetProgramDetailsQuery query = new GetProgramDetailsQuery(programId);
        GetProgramDetailsQueryResult result = queryExecutor.execute(query);
        return new GetProgramDetailsResponse(result.program(), cipher);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    UpdateProgramResponse update(@Valid @RequestBody UpdateProgramRequest request) {
        UpdateProgramCommand command = new UpdateProgramCommand(request, cipher);
        UpdateProgramCommandResult result = commandExecutor.execute(command);
        return new UpdateProgramResponse(result.message());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    GetAllProgramsResponse getAll() {
        GetAllProgramsQuery query = new GetAllProgramsQuery();
        GetAllProgramsQueryResult result = queryExecutor.execute(query);
        List<ProgramData> programData = result.programs().stream()
                .map(program -> new ProgramData(program, cipher))
                .toList();
        return new GetAllProgramsResponse(programData);
    }
}
