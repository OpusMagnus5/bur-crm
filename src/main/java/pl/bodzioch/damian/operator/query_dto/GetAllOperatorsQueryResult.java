package pl.bodzioch.damian.operator.query_dto;

import pl.bodzioch.damian.infrastructure.query.QueryResult;
import pl.bodzioch.damian.operator.OperatorDto;

import java.util.List;

public record GetAllOperatorsQueryResult(
        List<OperatorDto> operators
) implements QueryResult {
}
