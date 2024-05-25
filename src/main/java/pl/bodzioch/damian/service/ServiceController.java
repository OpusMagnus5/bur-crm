package pl.bodzioch.damian.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.dto.CreateNewServiceRequest;
import pl.bodzioch.damian.dto.CreateNewServiceResponse;
import pl.bodzioch.damian.dto.GetAllServiceTypesResponse;
import pl.bodzioch.damian.dto.GetServiceFromBurResponse;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.service.command_dto.CreateNewServiceCommand;
import pl.bodzioch.damian.service.command_dto.CreateNewServiceCommandResult;
import pl.bodzioch.damian.utils.CipherComponent;

import java.util.Arrays;
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
        List<String> typeList = Arrays.stream(ServiceType.values())
                .map(ServiceType::name)
                .toList();
        return new GetAllServiceTypesResponse(typeList);
    }
}
