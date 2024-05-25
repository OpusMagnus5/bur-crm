package pl.bodzioch.damian.service.query_dto;

import pl.bodzioch.damian.client.bur.BurServiceDto;
import pl.bodzioch.damian.infrastructure.query.QueryResult;

public record GetServiceFromBurQueryResult(
        BurServiceDto burServiceDto
) implements QueryResult {
}
