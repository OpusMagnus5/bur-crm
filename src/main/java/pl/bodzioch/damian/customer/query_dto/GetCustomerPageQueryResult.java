package pl.bodzioch.damian.customer.query_dto;

import pl.bodzioch.damian.customer.CustomerDto;
import pl.bodzioch.damian.infrastructure.query.QueryResult;

import java.util.List;

public record GetCustomerPageQueryResult(
        List<CustomerDto> customers,
        long totalCustomers
) implements QueryResult {
}
