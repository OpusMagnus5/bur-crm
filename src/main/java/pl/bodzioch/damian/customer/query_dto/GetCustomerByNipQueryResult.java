package pl.bodzioch.damian.customer.query_dto;

import pl.bodzioch.damian.customer.CustomerDto;
import pl.bodzioch.damian.infrastructure.query.QueryResult;

public record GetCustomerByNipQueryResult(
        CustomerDto customer
) implements QueryResult {
}
