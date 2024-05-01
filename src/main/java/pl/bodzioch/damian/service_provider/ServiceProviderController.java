package pl.bodzioch.damian.service_provider;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.dto.CreateNewServiceProviderRequest;
import pl.bodzioch.damian.dto.CreateNewServiceProviderResponse;
import pl.bodzioch.damian.dto.ProviderExistsResponse;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.service_provider.command_dto.CreateNewServiceProviderCommand;
import pl.bodzioch.damian.service_provider.command_dto.CreateNewServiceProviderCommandResult;
import pl.bodzioch.damian.service_provider.query_dto.GetServiceProviderByNipQuery;
import pl.bodzioch.damian.utils.validator.ProviderIdKindV;

@RestController
@RequestMapping("/api/service-provider")
@RequiredArgsConstructor
class ServiceProviderController {

    private final CommandExecutor commandExecutor;
    private final QueryExecutor queryExecutor;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateNewServiceProviderResponse createNew(@Valid @RequestBody CreateNewServiceProviderRequest request) {
        CreateNewServiceProviderCommand command = new CreateNewServiceProviderCommand(request.name(), Long.parseLong(request.nip()), 1L);
        CreateNewServiceProviderCommandResult result = commandExecutor.execute(command);
        return new CreateNewServiceProviderResponse(result.message());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/exists")
    ProviderExistsResponse isProviderExists(
            @RequestParam @ProviderIdKindV(message = "error.client.incorrectIdKind") String kindOfId,
            @RequestParam String id) {
        GetServiceProviderByNipQuery query = new GetServiceProviderByNipQuery(Long.parseLong(id));
        try {
            queryExecutor.execute(query);
        } catch (AppException e) {
            return new ProviderExistsResponse(false);
        }
        return new ProviderExistsResponse(true);
    }
}
