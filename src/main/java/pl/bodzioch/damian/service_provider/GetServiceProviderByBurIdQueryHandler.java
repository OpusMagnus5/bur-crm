package pl.bodzioch.damian.service_provider;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.service_provider.query_dto.GetServiceProviderByBurIdQuery;
import pl.bodzioch.damian.service_provider.query_dto.GetServiceProviderByBurIdQueryResult;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetServiceProviderByBurIdQueryHandler implements QueryHandler<GetServiceProviderByBurIdQuery, GetServiceProviderByBurIdQueryResult> {

    private final IProviderReadRepository readRepository;

    @Override
    public Class<GetServiceProviderByBurIdQuery> queryClass() {
        return GetServiceProviderByBurIdQuery.class;
    }

    @Override
    public GetServiceProviderByBurIdQueryResult handle(GetServiceProviderByBurIdQuery query) {
        ServiceProvider serviceProvider = readRepository.getByBurId(query.burId())
                .orElseThrow(() -> buildServiceProviderByBurIdNotFound(query.burId()));
        return new GetServiceProviderByBurIdQueryResult(new ServiceProviderDto(serviceProvider));
    }

    private AppException buildServiceProviderByBurIdNotFound(Long id) {
        return new AppException(
                "Service Provider with bur id: " + id + " not found",
                HttpStatus.NOT_FOUND,
                List.of(new ErrorData("error.client.serviceProviderByBurIdNotFound", List.of(id.toString())))
        );
    }
}
