package pl.bodzioch.damian.customer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.customer.command_dto.*;
import pl.bodzioch.damian.customer.query_dto.*;
import pl.bodzioch.damian.dto.*;
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
        CreateNewCustomerCommand command = new CreateNewCustomerCommand(request, cipher);
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

    @GetMapping(params = { "pageNumber", "pageSize" })
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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    DeleteCustomerResponse delete(@PathVariable String id) {
        long customerId = Long.parseLong(cipher.decryptMessage(id));
        DeleteCustomerCommand command = new DeleteCustomerCommand(customerId);
        DeleteCustomerCommandResult result = commandExecutor.execute(command);
        return new DeleteCustomerResponse(result.message());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    CustomerDetailsResponse getDetails(@PathVariable String id) {
        long customerId = Long.parseLong(cipher.decryptMessage(id));
        GetCustomerDetailsQuery query = new GetCustomerDetailsQuery(customerId);
        GetCustomerDetailsQueryResult result = queryExecutor.execute(query);
        return new CustomerDetailsResponse(result.customer(), cipher);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    UpdateCustomerResponse update(@Valid @RequestBody UpdateCustomerRequest request) {
        UpdateCustomerCommand command = new UpdateCustomerCommand(request, cipher);
        UpdateCustomerCommandResult result = commandExecutor.execute(command);
        return new UpdateCustomerResponse(result.message());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    GetAllCustomersResponse getAll() {
        GetAllCustomersQuery query = new GetAllCustomersQuery();
        GetAllCustomersQueryResult result = queryExecutor.execute(query);
        List<CustomerData> customers = result.customers().stream()
                .map(customer -> new CustomerData(customer, cipher))
                .toList();
        return new GetAllCustomersResponse(customers);
    }
}
