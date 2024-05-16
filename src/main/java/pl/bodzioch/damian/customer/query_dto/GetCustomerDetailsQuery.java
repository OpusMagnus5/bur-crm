package pl.bodzioch.damian.customer.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetCustomerDetailsQuery(
        Long id
) implements Query<GetCustomerDetailsQueryResult> {
}
