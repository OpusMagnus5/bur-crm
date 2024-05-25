package pl.bodzioch.damian.customer.query_dto;

import pl.bodzioch.damian.customer.CustomerDto;
import pl.bodzioch.damian.infrastructure.query.QueryResult;

import java.util.List;

public record GetAllCustomersQueryResult(
        List<CustomerDto> customers
) implements QueryResult {
}
