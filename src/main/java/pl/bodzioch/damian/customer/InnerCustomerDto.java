package pl.bodzioch.damian.customer;

import java.util.Optional;

public record InnerCustomerDto(
        Long id,
        String name
) {

    public InnerCustomerDto(InnerCustomer customer) {
        this(
                Optional.ofNullable(customer).map(InnerCustomer::id).orElse(null),
                Optional.ofNullable(customer).map(InnerCustomer::name).orElse(null)
        );
    }
}
