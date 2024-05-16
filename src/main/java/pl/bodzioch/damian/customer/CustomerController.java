package pl.bodzioch.damian.customer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.customer.command_dto.CreateNewCustomerCommand;
import pl.bodzioch.damian.customer.command_dto.CreateNewCustomerCommandResult;
import pl.bodzioch.damian.customer.query_dto.GetCustomerByNipQuery;
import pl.bodzioch.damian.dto.CreateNewCustomerRequest;
import pl.bodzioch.damian.dto.CreateNewCustomerResponse;
import pl.bodzioch.damian.dto.CustomerExistsResponse;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.utils.CipherComponent;

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
    @GetMapping("/exists")
    CustomerExistsResponse isProviderExists(@RequestParam String nip) {
        GetCustomerByNipQuery query = new GetCustomerByNipQuery(Long.parseLong(nip));
        try {
            queryExecutor.execute(query);
        } catch (AppException e) {
            return new CustomerExistsResponse(false);
        }
        return new CustomerExistsResponse(true);
    }
}
