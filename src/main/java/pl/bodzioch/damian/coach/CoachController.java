package pl.bodzioch.damian.coach;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.pl.PESEL;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.coach.command_dto.*;
import pl.bodzioch.damian.coach.query_dto.*;
import pl.bodzioch.damian.dto.*;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.utils.CipherComponent;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/coach")
@Validated
@RequiredArgsConstructor
class CoachController {

    private final CommandExecutor commandExecutor;
    private final QueryExecutor queryExecutor;
    private final CipherComponent cipher;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateNewCoachResponse createNew(@Valid @RequestBody CreateNewCoachRequest request) {
        CreateNewCoachCommand command = new CreateNewCoachCommand(request, cipher);
        CreateNewCoachCommandResult result = commandExecutor.execute(command);
        return new CreateNewCoachResponse(result.message());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/exists")
    CoachExistsResponse isUserExists(@RequestParam @PESEL(message = "error.client.coach.incorrectPesel") String pesel) {
        GetCoachByPeselQuery query = new GetCoachByPeselQuery(pesel);
        try {
            queryExecutor.execute(query);
            return new CoachExistsResponse(true);
        } catch (AppException e) {
            return new CoachExistsResponse(false);
        }
    }

    @GetMapping(params = { "pageNumber", "pageSize" })
    @ResponseStatus(HttpStatus.OK)
    CoachPageResponse getCoaches(
            @RequestParam
            @Min(value = 1, message = "error.client.minPageNumber")
            @Max(value = Integer.MAX_VALUE, message = "error.client.maxPageNumber")
            int pageNumber,
            @Min(value = 10, message = "error.client.minPageSize")
            @Max(value = 50, message = "error.client.maxPageSize")
            @RequestParam int pageSize,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName){
        HashMap<CoachFilterField, Object> filters = new HashMap<>();
        filters.put(CoachFilterField.FIRST_NAME, firstName);
        filters.put(CoachFilterField.LAST_NAME, lastName);
        GetCoachPageQuery query = new GetCoachPageQuery(pageNumber, pageSize, filters);
        GetCoachPageQueryResult result = queryExecutor.execute(query);
        List<CoachData> coachData = result.coaches().stream()
                .map(element -> new CoachData(element, cipher))
                .toList();
        return new CoachPageResponse(coachData, result.totalCoaches());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    DeleteCoachResponse delete(@PathVariable String id) {
        long coachId = Long.parseLong(cipher.decryptMessage(id));
        DeleteCoachCommand command = new DeleteCoachCommand(coachId);
        DeleteCoachCommandResult result = commandExecutor.execute(command);
        return new DeleteCoachResponse(result.message());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    CoachDetailsResponse getDetails(@PathVariable String id) {
        long coachId = Long.parseLong(cipher.decryptMessage(id));
        GetCoachDetailsQuery query = new GetCoachDetailsQuery(coachId);
        GetCoachDetailsQueryResult result = queryExecutor.execute(query);
        return new CoachDetailsResponse(result.coach(), cipher);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    UpdateCoachResponse update(@Valid @RequestBody UpdateCoachRequest request) {
        UpdateCoachCommand command = new UpdateCoachCommand(request, cipher);
        UpdateCoachCommandResult result = commandExecutor.execute(command);
        return new UpdateCoachResponse(result.message());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    GetAllCoachesResponse getAll() {
        GetAllCoachesQuery query = new GetAllCoachesQuery();
        GetAllCoachesQueryResult result = queryExecutor.execute(query);
        List<CoachData> coaches = result.coaches().stream()
                .map(coach -> new CoachData(coach, cipher))
                .toList();
        return new GetAllCoachesResponse(coaches);
    }
}