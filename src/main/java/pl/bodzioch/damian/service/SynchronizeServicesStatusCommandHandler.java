package pl.bodzioch.damian.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.client.bur.IBurClient;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.service.command_dto.SynchronizeServicesStatusCommand;
import pl.bodzioch.damian.service.command_dto.SynchronizeServicesStatusCommandResult;

import java.util.AbstractMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
class SynchronizeServicesStatusCommandHandler implements CommandHandler<SynchronizeServicesStatusCommand, SynchronizeServicesStatusCommandResult> {

    private final IServiceReadRepository readRepository;
    private final IServiceWriteRepository writeRepository;
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
                    .map(item -> {
                        try {
                            return new AbstractMap.SimpleImmutableEntry<>(item.getKey(), item.getValue().join());
                        } catch (Exception e) {
                            log.error("Error while completing future with sercice");
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .map(item -> new Service(item.getKey(), item.getValue()))
                    .toList();
            writeRepository.updateStatus(synchronizedServices);
        }
        return new SynchronizeServicesStatusCommandResult();
    }
}
