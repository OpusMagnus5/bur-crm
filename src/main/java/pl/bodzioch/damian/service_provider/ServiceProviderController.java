package pl.bodzioch.damian.service_provider;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.pl.NIP;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.dto.CreateNewServiceProviderRequest;
import pl.bodzioch.damian.dto.CreateNewServiceProviderResponse;
import pl.bodzioch.damian.dto.ProviderExistsResponse;
import pl.bodzioch.damian.dto.ProviderNameResponse;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.service_provider.command_dto.CreateNewServiceProviderCommand;
import pl.bodzioch.damian.service_provider.command_dto.CreateNewServiceProviderCommandResult;
import pl.bodzioch.damian.service_provider.command_dto.GetProviderNameByNipFromBurCommand;
import pl.bodzioch.damian.service_provider.command_dto.GetProviderNameByNipFromBurCommandResult;
import pl.bodzioch.damian.service_provider.query_dto.GetServiceProviderByNipQuery;
import pl.bodzioch.damian.utils.validator.ProviderIdKindV;

@RestController
@RequestMapping("/api/service-provider")
@RequiredArgsConstructor
@Validated
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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/name")
    ProviderNameResponse getName(@RequestParam @NIP(message = "error.client.serviceProvider.createNew.incorrectNIP") String nip) {
        GetProviderNameByNipFromBurCommand command = new GetProviderNameByNipFromBurCommand(nip);
        GetProviderNameByNipFromBurCommandResult result = commandExecutor.execute(command);
        return new ProviderNameResponse(result.name());
    }
}
