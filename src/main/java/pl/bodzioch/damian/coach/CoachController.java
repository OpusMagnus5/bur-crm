package pl.bodzioch.damian.coach;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.pl.PESEL;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.coach.command_dto.CreateNewCoachCommand;
import pl.bodzioch.damian.coach.command_dto.CreateNewCoachCommandResult;
import pl.bodzioch.damian.coach.command_dto.DeleteCoachCommand;
import pl.bodzioch.damian.coach.command_dto.DeleteCoachCommandResult;
import pl.bodzioch.damian.coach.query_dto.GetCoachByPeselQuery;
import pl.bodzioch.damian.coach.query_dto.GetCoachPageQuery;
import pl.bodzioch.damian.coach.query_dto.GetCoachPageQueryResult;
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
        CreateNewCoachCommand command = new CreateNewCoachCommand(request);
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

    @GetMapping
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
}