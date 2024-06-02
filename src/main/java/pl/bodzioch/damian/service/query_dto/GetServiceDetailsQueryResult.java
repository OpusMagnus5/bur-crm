package pl.bodzioch.damian.service.query_dto;

import pl.bodzioch.damian.infrastructure.query.QueryResult;
import pl.bodzioch.damian.service.ServiceDto;

public record GetServiceDetailsQueryResult(
        ServiceDto service
) implements QueryResult {
}
