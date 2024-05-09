package pl.bodzioch.damian.program;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.dto.CreateNewProgramRequest;
import pl.bodzioch.damian.dto.CreateNewProgramResponse;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.program.command_dto.CreateNewProgramCommand;
import pl.bodzioch.damian.program.command_dto.CreateNewProgramCommandResult;
import pl.bodzioch.damian.utils.CipherComponent;

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
}
