package pl.bodzioch.damian.service_provider;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.pl.NIP;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.dto.*;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.service_provider.command_dto.*;
import pl.bodzioch.damian.service_provider.query_dto.*;
import pl.bodzioch.damian.utils.CipherComponent;

import java.util.List;

@RestController
@RequestMapping("/api/service-provider")
@RequiredArgsConstructor
@Validated
class ServiceProviderController {

    private final CommandExecutor commandExecutor;
    private final QueryExecutor queryExecutor;
    private final CipherComponent cipher;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateNewServiceProviderResponse createNew(@Valid @RequestBody CreateNewServiceProviderRequest request) {
        CreateNewServiceProviderCommand command = new CreateNewServiceProviderCommand(request.name(), Long.parseLong(request.nip()), 1L);
        CreateNewServiceProviderCommandResult result = commandExecutor.execute(command);
        return new CreateNewServiceProviderResponse(result.message());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/exists")
    ProviderExistsResponse isProviderExists(@RequestParam String nip) {
        GetServiceProviderByNipQuery query = new GetServiceProviderByNipQuery(Long.parseLong(nip));
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ServiceProviderPageResponse getServiceProviders(
            @RequestParam
            @Min(value = 1, message = "error.client.minPageNumber")
            @Max(value = Integer.MAX_VALUE, message = "error.client.maxPageNumber")
            int pageNumber,
            @Min(value = 10, message = "error.client.minPageSize")
            @Max(value = 50, message = "error.client.maxPageSize")
            @RequestParam int pageSize){
        GetProvidersPageQuery query = new GetProvidersPageQuery(pageNumber, pageSize);
        GetProvidersPageQueryResult result = queryExecutor.execute(query);
        List<ServiceProviderData> providersData = result.providers().stream()
                .map(element -> new ServiceProviderData(element, cipher))
                .toList();
        return new ServiceProviderPageResponse(providersData, result.totalProviders());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    GetServiceProviderDetailsResponse getDetails(@PathVariable String id) {
        long providerId = Long.parseLong(cipher.decryptMessage(id));
        GetServiceProviderDetailsQuery query = new GetServiceProviderDetailsQuery(providerId);
        GetServiceProviderDetailsQueryResult result = queryExecutor.execute(query);
        return new GetServiceProviderDetailsResponse(result.serviceProvider(), cipher);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    DeleteServiceProviderResponse delete(@PathVariable String id) {
        long providerId = Long.parseLong(cipher.decryptMessage(id));
        DeleteServiceProviderCommand command = new DeleteServiceProviderCommand(providerId);
        DeleteServiceProviderCommandResult result = commandExecutor.execute(command);
        return new DeleteServiceProviderResponse(result.message());
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    UpdateServiceProviderResponse update(@Valid @RequestBody UpdateServiceProviderRequest request) {
        long providerId = Long.parseLong(cipher.decryptMessage(request.id()));
        UpdateServiceProviderCommand command = new UpdateServiceProviderCommand(
                providerId,
                request.version(),
                request.name(),
                Long.parseLong(request.nip()),
                1L);
        UpdateServiceProviderCommandResult result = commandExecutor.execute(command);
        return new UpdateServiceProviderResponse(result.message());
    }
}
