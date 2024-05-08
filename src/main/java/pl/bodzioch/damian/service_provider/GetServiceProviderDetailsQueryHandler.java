package pl.bodzioch.damian.service_provider;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.service_provider.query_dto.GetServiceProviderDetailsQuery;
import pl.bodzioch.damian.service_provider.query_dto.GetServiceProviderDetailsQueryResult;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetServiceProviderDetailsQueryHandler implements QueryHandler<GetServiceProviderDetailsQuery, GetServiceProviderDetailsQueryResult> {

    private final IProviderReadRepository readRepository;

    @Override
    public Class<GetServiceProviderDetailsQuery> queryClass() {
        return GetServiceProviderDetailsQuery.class;
    }

    @Override
    public GetServiceProviderDetailsQueryResult handle(GetServiceProviderDetailsQuery query) {
        ServiceProvider serviceProvider = readRepository.getDetails(query.id()).orElseThrow(() -> buildServiceProviderByIdNotFound(query.id()));
        return new GetServiceProviderDetailsQueryResult(new ServiceProviderDto(serviceProvider));

    }

    private AppException buildServiceProviderByIdNotFound(Long id) {
        return new AppException(
                "No service provider with id: " + id,
                HttpStatus.NOT_FOUND,
                List.of(new ErrorData("error.client.serviceProviderByIdNotFound", List.of(id.toString())))
        );
    }
}
