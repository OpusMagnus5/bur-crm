package pl.bodzioch.damian.service_provider;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bodzioch.damian.dto.CreateNewServiceProviderRequest;
import pl.bodzioch.damian.dto.CreateNewServiceProviderResponse;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;

@RestController
@RequestMapping("/api/service-provider")
@RequiredArgsConstructor
public class ServiceProviderController {

    private final CommandExecutor commandExecutor;

    CreateNewServiceProviderResponse createNew(@Valid @RequestBody CreateNewServiceProviderRequest request) {
        return null;
    }
}
