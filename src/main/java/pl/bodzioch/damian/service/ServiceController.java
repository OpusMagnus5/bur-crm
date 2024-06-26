package pl.bodzioch.damian.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.dto.*;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.service.command_dto.CreateOrUpdateServiceCommand;
import pl.bodzioch.damian.service.command_dto.CreateOrUpdateServiceCommandResult;
import pl.bodzioch.damian.service.command_dto.DeleteServiceCommand;
import pl.bodzioch.damian.service.command_dto.DeleteServiceCommandResult;
import pl.bodzioch.damian.service.query_dto.GetServiceDetailsQuery;
import pl.bodzioch.damian.service.query_dto.GetServiceDetailsQueryResult;
import pl.bodzioch.damian.service.query_dto.GetServicePageQuery;
import pl.bodzioch.damian.service.query_dto.GetServicePageQueryResult;
import pl.bodzioch.damian.utils.CipherComponent;
import pl.bodzioch.damian.utils.MessageResolver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static pl.bodzioch.damian.service.ServiceFilterField.*;

@RestController
@RequestMapping("/api/service")
@RequiredArgsConstructor
@Validated
class ServiceController {

    private final CommandExecutor commandExecutor;
    private final QueryExecutor queryExecutor;
    private final CipherComponent cipher;
    private final IServiceService serviceService;
    private final MessageResolver messageResolver;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrUpdateServiceResponse createOrUpdate(@Valid @RequestBody CreateOrUpdateServiceRequest request) {
        CreateOrUpdateServiceCommand command = new CreateOrUpdateServiceCommand(request, cipher);
        CreateOrUpdateServiceCommandResult result = commandExecutor.execute(command);
        return new CreateOrUpdateServiceResponse(result.messages());
    }

    @GetMapping(params = { "number" })
    @ResponseStatus(HttpStatus.OK)
    GetServiceFromBurResponse getFromBur(
            @RequestParam
            @Pattern(regexp = "\\d{4}/\\d{2}/\\d{2}/\\d+/\\d+", message = "error.client.service.incorrectNumber")
            String number
    ) {
        return serviceService.getServiceFromBur(number);
    }

    @GetMapping("/types")
    @ResponseStatus(HttpStatus.OK)
    GetAllServiceTypesResponse getAllServiceTypes() {
        List<ServiceTypeData> typeList = Arrays.stream(ServiceType.values())
                .map(ServiceType::name)
                .map(val -> new ServiceTypeData(val, messageResolver.getMessage("service.type." + val)))
                .toList();
        return new GetAllServiceTypesResponse(typeList);
    }

    @GetMapping(params = { "pageNumber", "pageSize" })
    @ResponseStatus(HttpStatus.OK)
    ServicePageResponse getServiceProviders(
            @RequestParam
            @Min(value = 1, message = "error.client.minPageNumber")
            @Max(value = Integer.MAX_VALUE, message = "error.client.maxPageNumber")
            int pageNumber,
            @Min(value = 10, message = "error.client.minPageSize")
            @Max(value = 50, message = "error.client.maxPageSize")
            @RequestParam int pageSize,
            @RequestParam(required = false) String number,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String serviceProviderId,
            @RequestParam(required = false) String customerId){
        HashMap<ServiceFilterField, Object> filters = new HashMap<>();
        filters.put(NUMBER, number);
        filters.put(TYPE, type);
        filters.put(SERVICE_PROVIDER_ID, cipher.getDecryptedId(serviceProviderId));
        filters.put(CUSTOMER_ID, cipher.getDecryptedId(customerId));

        GetServicePageQuery query = new GetServicePageQuery(pageNumber, pageSize, filters);
        GetServicePageQueryResult result = queryExecutor.execute(query);
        List<ServiceData> servicesData = result.services().stream()
                .map(element -> new ServiceData(element, cipher, messageResolver))
                .toList();
        return new ServicePageResponse(servicesData, result.totalServices());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    GetServiceDetailsResponse getDetails(@PathVariable String id) {
        long serviceId = Long.parseLong(cipher.decryptMessage(id));
        GetServiceDetailsQuery query = new GetServiceDetailsQuery(serviceId);
        GetServiceDetailsQueryResult result = queryExecutor.execute(query);
        return new GetServiceDetailsResponse(result.service(), messageResolver, cipher);
    }

    @GetMapping("/statuses")
    @ResponseStatus(HttpStatus.OK)
    GetAllServiceStatusesResponse getAllServiceStatuses() {
        List<ServiceStatusData> typeList = Arrays.stream(ServiceStatus.values())
                .map(Enum::name)
                .map(val -> new ServiceStatusData(val, messageResolver.getMessage("service.status." + val)))
                .toList();
        return new GetAllServiceStatusesResponse(typeList);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    DeleteServiceResponse deleteService(@PathVariable
                                        @NotEmpty(message = "error.client.service.emptyServiceId")
                                        String id
    ) {
        long serviceId = Long.parseLong(cipher.decryptMessage(id));
        DeleteServiceCommand command = new DeleteServiceCommand(serviceId);
        DeleteServiceCommandResult result = commandExecutor.execute(command);
        return new DeleteServiceResponse(result.message());
    }
}