package pl.bodzioch.damian.customer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.customer.command_dto.CreateNewCustomerCommand;
import pl.bodzioch.damian.customer.command_dto.CreateNewCustomerCommandResult;
import pl.bodzioch.damian.customer.query_dto.CustomerData;
import pl.bodzioch.damian.customer.query_dto.GetCustomerByNipQuery;
import pl.bodzioch.damian.customer.query_dto.GetCustomerPageQuery;
import pl.bodzioch.damian.customer.query_dto.GetCustomerPageQueryResult;
import pl.bodzioch.damian.dto.CreateNewCustomerRequest;
import pl.bodzioch.damian.dto.CreateNewCustomerResponse;
import pl.bodzioch.damian.dto.CustomerExistsResponse;
import pl.bodzioch.damian.dto.CustomerPageResponse;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.utils.CipherComponent;

import java.util.HashMap;
import java.util.List;

import static pl.bodzioch.damian.customer.CustomerFilterField.NAME;
import static pl.bodzioch.damian.customer.CustomerFilterField.NIP;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@Validated
class CustomerController {

    private final CommandExecutor commandExecutor;
    private final QueryExecutor queryExecutor;
    private final CipherComponent cipher;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateNewCustomerResponse createNew(@Valid @RequestBody CreateNewCustomerRequest request) {
        CreateNewCustomerCommand command = new CreateNewCustomerCommand(request);
        CreateNewCustomerCommandResult result = commandExecutor.execute(command);
        return new CreateNewCustomerResponse(result.message());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/exists", params = {"nip"})
    CustomerExistsResponse isProviderExists(@RequestParam String nip) {
        GetCustomerByNipQuery query = new GetCustomerByNipQuery(Long.parseLong(nip));
        try {
            queryExecutor.execute(query);
        } catch (AppException e) {
            return new CustomerExistsResponse(false);
        }
        return new CustomerExistsResponse(true);
    }

    @GetMapping(params = {"pageNumber", "pageSize"})
    @ResponseStatus(HttpStatus.OK)
    CustomerPageResponse getServiceProviders(
            @RequestParam
            @Min(value = 1, message = "error.client.minPageNumber")
            @Max(value = Integer.MAX_VALUE, message = "error.client.maxPageNumber")
            int pageNumber,
            @Min(value = 10, message = "error.client.minPageSize")
            @Max(value = 50, message = "error.client.maxPageSize")
            @RequestParam int pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String nip){
        HashMap<CustomerFilterField, Object> filters = new HashMap<>();
        filters.put(NAME, name);
        filters.put(NIP, NumberUtils.isParsable(nip) ? Long.parseLong(nip) : null);
        GetCustomerPageQuery query = new GetCustomerPageQuery(pageNumber, pageSize, filters);
        GetCustomerPageQueryResult result = queryExecutor.execute(query);
        List<CustomerData> operatorData = result.customers().stream()
                .map(element -> new CustomerData(element, cipher))
                .toList();
        return new CustomerPageResponse(operatorData, result.totalCustomers());
    }
}
