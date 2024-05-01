package pl.bodzioch.damian.service_provider;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.service_provider.query_dto.GetServiceProviderByNipQuery;
import pl.bodzioch.damian.service_provider.query_dto.GetServiceProviderByNipQueryResult;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class GetServiceProviderByNipQueryHandler implements QueryHandler<GetServiceProviderByNipQuery, GetServiceProviderByNipQueryResult> {

    private final IProviderReadRepository readRepository;

    @Override
    public Class<GetServiceProviderByNipQuery> queryClass() {
        return GetServiceProviderByNipQuery.class;
    }

    @Override
    public GetServiceProviderByNipQueryResult handle(GetServiceProviderByNipQuery query) {
        Optional<ServiceProvider> serviceProvider = readRepository.getByNip(query.nip());
        if (serviceProvider.isEmpty()) {
            throw buildServiceProviderByNipNotFound(query.nip());
        }
        return new GetServiceProviderByNipQueryResult(serviceProvider.get().toDto());
    }

    private AppException buildServiceProviderByNipNotFound(Long nip) {
        return new AppException(
                "Service Provider with nip: " + nip + " not found",
                HttpStatus.NOT_FOUND,
                List.of(new ErrorData("error.client.serviceProviderByNipNotFound", List.of(nip.toString())))
        );
    }
}
