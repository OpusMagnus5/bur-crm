package pl.bodzioch.damian.service.query_dto;

import pl.bodzioch.damian.infrastructure.query.QueryResult;
import pl.bodzioch.damian.service.ServiceDto;

import java.util.List;

public record GetServicePageQueryResult(
        List<ServiceDto> services,
        long totalServices
) implements QueryResult {
}
