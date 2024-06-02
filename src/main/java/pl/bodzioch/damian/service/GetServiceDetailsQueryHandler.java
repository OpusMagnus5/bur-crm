package pl.bodzioch.damian.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.service.query_dto.GetServiceDetailsQuery;
import pl.bodzioch.damian.service.query_dto.GetServiceDetailsQueryResult;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetServiceDetailsQueryHandler implements QueryHandler<GetServiceDetailsQuery, GetServiceDetailsQueryResult> {

    private final IServiceReadRepository readRepository;

    @Override
    public Class<GetServiceDetailsQuery> queryClass() {
        return GetServiceDetailsQuery.class;
    }

    @Override
    public GetServiceDetailsQueryResult handle(GetServiceDetailsQuery query) {
        Service service = readRepository.getDetails(query.id()).orElseThrow(() -> buildServiceByIdNotFound(query.id()));
        return new GetServiceDetailsQueryResult(new ServiceDto(service));
    }

    private AppException buildServiceByIdNotFound(Long id) {
        return new AppException(
                "Not found service with id: " + id,
                HttpStatus.NOT_FOUND,
                List.of(new ErrorData("error.client.service.byIdNotFound", List.of(id.toString())))
        );
    }
}
