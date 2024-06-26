package pl.bodzioch.damian.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.client.bur.BurServiceDto;
import pl.bodzioch.damian.dto.GetServiceFromBurResponse;
import pl.bodzioch.damian.error.ErrorDto;
import pl.bodzioch.damian.error.command_dto.SaveErrorCommand;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.exception.HttpClientException;
import pl.bodzioch.damian.exception.ServerException;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.service.command_dto.SynchronizeServicesStatusCommand;
import pl.bodzioch.damian.service.query_dto.GetServiceFromBurQuery;
import pl.bodzioch.damian.service.query_dto.GetServiceFromBurQueryResult;
import pl.bodzioch.damian.service_provider.ServiceProviderDto;
import pl.bodzioch.damian.service_provider.query_dto.GetServiceProviderByBurIdQuery;
import pl.bodzioch.damian.service_provider.query_dto.GetServiceProviderByBurIdQueryResult;
import pl.bodzioch.damian.utils.MessageResolver;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
class ServiceService implements IServiceService {

    private final QueryExecutor queryExecutor;
    private final CommandExecutor commandExecutor;
    private final MessageResolver messageResolver;

    @Override
    public GetServiceFromBurResponse getServiceFromBur(String serviceNumber) {
        try {
            GetServiceFromBurQuery burQuery = new GetServiceFromBurQuery(serviceNumber);
            GetServiceFromBurQueryResult burQueryResult = queryExecutor.execute(burQuery);
            BurServiceDto burServiceDto = burQueryResult.burServiceDto();

            Long burServiceProviderId = burServiceDto.serviceProvider().id();
            GetServiceProviderByBurIdQuery serviceProviderQuery = new GetServiceProviderByBurIdQuery(burServiceProviderId);
            GetServiceProviderByBurIdQueryResult serviceProviderResult = queryExecutor.execute(serviceProviderQuery);
            ServiceProviderDto serviceProviderDto = serviceProviderResult.serviceProviderDto();

            return new GetServiceFromBurResponse(burServiceDto, serviceProviderDto.name(), messageResolver);
        } catch (ServerException | HttpClientException e) {
            throw new AppException(
                    "Service in BUR with given service number " + serviceNumber + " not found",
                    HttpStatus.NOT_FOUND,
                    List.of(new ErrorData("error.client.service.serviceByNumberNotFoundInBur", List.of(serviceNumber)))
            );
        }
    }

    @Scheduled(cron = "0 0 23 * * *")
    @Transactional(Transactional.TxType.REQUIRED)
    public void synchronizeServicesStatus() {
        try {
            SynchronizeServicesStatusCommand command = new SynchronizeServicesStatusCommand();
            commandExecutor.execute(command);
        } catch (Exception e) {
            log.error("Synchronize services status failed", e);
            ErrorDto errorDto = new ErrorDto(e);
            SaveErrorCommand command = new SaveErrorCommand(errorDto);
            commandExecutor.execute(command);
        }
    }
}
