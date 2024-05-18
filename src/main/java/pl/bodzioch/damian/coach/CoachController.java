package pl.bodzioch.damian.coach;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.pl.PESEL;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.coach.command_dto.CreateNewCoachCommand;
import pl.bodzioch.damian.coach.command_dto.CreateNewCoachCommandResult;
import pl.bodzioch.damian.coach.query_dto.GetCoachByPeselQuery;
import pl.bodzioch.damian.dto.CoachExistsResponse;
import pl.bodzioch.damian.dto.CreateNewCoachRequest;
import pl.bodzioch.damian.dto.CreateNewCoachResponse;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.utils.CipherComponent;

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
}