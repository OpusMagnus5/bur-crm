package pl.bodzioch.damian.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.client.bur.BurServiceDto;
import pl.bodzioch.damian.client.bur.IBurClient;
import pl.bodzioch.damian.exception.ServerException;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.service.query_dto.GetServiceFromBurQuery;
import pl.bodzioch.damian.service.query_dto.GetServiceFromBurQueryResult;

@Slf4j
@Component
@RequiredArgsConstructor
class GetServiceFromBurQueryHandler implements QueryHandler<GetServiceFromBurQuery, GetServiceFromBurQueryResult> {

    private final IBurClient burClient;

    @Override
    public Class<GetServiceFromBurQuery> queryClass() {
        return GetServiceFromBurQuery.class;
    }

    @Override
    public GetServiceFromBurQueryResult handle(GetServiceFromBurQuery query) {
        Long burServiceId = new BurServiceNumber(query.serviceNumber()).getBurServiceId();
        BurServiceDto service = burClient.getService(burServiceId);
        if (service == null) {
            log.warn("Service in BUR with id {} not found!", burServiceId);
            throw new ServerException("Service in BUR not found!");
        }
        return new GetServiceFromBurQueryResult(service);
    }
}
