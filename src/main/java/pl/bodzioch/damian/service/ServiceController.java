package pl.bodzioch.damian.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.dto.CreateNewServiceRequest;
import pl.bodzioch.damian.dto.CreateNewServiceResponse;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.utils.CipherComponent;

@RestController
@RequestMapping("/api/service")
@RequiredArgsConstructor
@Validated
class ServiceController {

    private final CommandExecutor commandExecutor;
    private final QueryExecutor queryExecutor;
    private final CipherComponent cipher;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateNewServiceResponse createNew(@Valid @RequestBody CreateNewServiceRequest request) {
        return null; //TODO dokończyć
    }
}
