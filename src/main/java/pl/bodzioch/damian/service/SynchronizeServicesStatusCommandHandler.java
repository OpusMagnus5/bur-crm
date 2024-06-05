package pl.bodzioch.damian.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.client.bur.IBurClient;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.service.command_dto.SynchronizeServicesStatusCommand;
import pl.bodzioch.damian.service.command_dto.SynchronizeServicesStatusCommandResult;

import java.util.AbstractMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
class SynchronizeServicesStatusCommandHandler implements CommandHandler<SynchronizeServicesStatusCommand, SynchronizeServicesStatusCommandResult> {

    private final IServiceReadRepository readRepository;
    private final IBurClient burClient;

    @Override
    public Class<SynchronizeServicesStatusCommand> commandClass() {
        return SynchronizeServicesStatusCommand.class;
    }

    @Override
    public SynchronizeServicesStatusCommandResult handle(SynchronizeServicesStatusCommand command) {
        List<Service> services = readRepository.getServicesToStatusCheck();
        if (services.isEmpty()) {
            return new SynchronizeServicesStatusCommandResult();
        }
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()){
            List<Service> synchronizedServices = services.stream()
                    .map(item -> new AbstractMap.SimpleImmutableEntry<>(
                            item, CompletableFuture.supplyAsync(() -> burClient.getService(item.burCardId()), executor)))
                    .map(item -> new AbstractMap.SimpleImmutableEntry<>(item.getKey(), item.getValue().join()))
                    .map(item -> new Service(item.getKey(), item.getValue()))
                    .toList();
            //TODO
        }
    }
}
