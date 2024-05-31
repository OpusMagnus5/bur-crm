package pl.bodzioch.damian.service.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;
import pl.bodzioch.damian.service.ServiceFilterField;

import java.util.Map;

public record GetServicePageQuery(
        int pageNumber,
        int pageSize,
        Map<ServiceFilterField, ?> filters
) implements Query<GetServicePageQueryResult> {
}
