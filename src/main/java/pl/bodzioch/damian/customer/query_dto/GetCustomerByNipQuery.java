package pl.bodzioch.damian.customer.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetCustomerByNipQuery(
        Long nip
) implements Query<GetCustomerByNipQueryResult> {
}
