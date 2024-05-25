package pl.bodzioch.damian.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.client.bur.BurServiceDto;
import pl.bodzioch.damian.dto.GetServiceFromBurResponse;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.exception.ServerException;
import pl.bodzioch.damian.infrastructure.query.QueryExecutor;
import pl.bodzioch.damian.service.query_dto.GetServiceFromBurQuery;
import pl.bodzioch.damian.service.query_dto.GetServiceFromBurQueryResult;
import pl.bodzioch.damian.service_provider.ServiceProviderDto;
import pl.bodzioch.damian.service_provider.query_dto.GetServiceProviderByBurIdQuery;
import pl.bodzioch.damian.service_provider.query_dto.GetServiceProviderByBurIdQueryResult;
import pl.bodzioch.damian.utils.CipherComponent;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Component
@RequiredArgsConstructor
class ServiceService implements IServiceService {

    private final QueryExecutor queryExecutor;
    private final CipherComponent cipherComponent;

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
            String encryptedServiceProviderId = cipherComponent.encryptMessage(serviceProviderDto.id().toString());

            return new GetServiceFromBurResponse(burServiceDto, encryptedServiceProviderId);
        } catch (ServerException e) {
            throw new AppException(
                    "Service in BUR with given service number " + serviceNumber + " not found",
                    HttpStatus.NOT_FOUND,
                    List.of(new ErrorData("error.client.service.serviceByNumberNotFoundInBur", List.of(serviceNumber)))
            );
        }
    }
}
