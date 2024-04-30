package pl.bodzioch.damian.service_provider;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.dto.CreateNewServiceProviderRequest;
import pl.bodzioch.damian.dto.CreateNewServiceProviderResponse;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.service_provider.command_dto.CreateNewServiceProviderCommand;
import pl.bodzioch.damian.service_provider.command_dto.CreateNewServiceProviderCommandResult;

@RestController
@RequestMapping("/api/service-provider")
@RequiredArgsConstructor
class ServiceProviderController {

    private final CommandExecutor commandExecutor;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateNewServiceProviderResponse createNew(@Valid @RequestBody CreateNewServiceProviderRequest request) {
        CreateNewServiceProviderCommand command = new CreateNewServiceProviderCommand(request.name(), Long.parseLong(request.nip()), 1L);
        CreateNewServiceProviderCommandResult result = commandExecutor.execute(command);
        return new CreateNewServiceProviderResponse(result.message());
    }
}
