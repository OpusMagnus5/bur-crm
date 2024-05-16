package pl.bodzioch.damian.customer.query_dto;

import pl.bodzioch.damian.customer.CustomerFilterField;
import pl.bodzioch.damian.infrastructure.query.Query;

import java.util.Map;

public record GetCustomerPageQuery(
        int pageNumber,
        int pageSize,
        Map<CustomerFilterField, ?> filters
) implements Query<GetCustomerPageQueryResult> {
}
