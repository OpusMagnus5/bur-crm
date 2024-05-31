package pl.bodzioch.damian.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.dto.*;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.service.command_dto.CreateNewServiceCommand;
import pl.bodzioch.damian.service.command_dto.CreateNewServiceCommandResult;
import pl.bodzioch.damian.service.query_dto.GetServicePageQuery;
import pl.bodzioch.damian.service.query_dto.GetServicePageQueryResult;
import pl.bodzioch.damian.utils.CipherComponent;
import pl.bodzioch.damian.utils.MessageResolver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
    CreateNewServiceResponse createNew(@Valid @RequestBody CreateNewServiceRequest request) {
        CreateNewServiceCommand command = new CreateNewServiceCommand(request, cipher);
        CreateNewServiceCommandResult result = commandExecutor.execute(command);
        return new CreateNewServiceResponse(result.messages());
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
            @RequestParam int pageSize){
        GetServicePageQuery query = new GetServicePageQuery(pageNumber, pageSize, new HashMap<>());
        GetServicePageQueryResult result = queryExecutor.execute(query);
        List<ServiceData> servicesData = result.services().stream()
                .map(element -> new ServiceData(element, cipher))
                .toList();
        return new ServicePageResponse(servicesData, result.totalServices());
    }
}
