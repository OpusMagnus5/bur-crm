package pl.bodzioch.damian.customer;

import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbConstructor;
import pl.bodzioch.damian.infrastructure.database.DbId;

public record InnerCustomer(
        @DbId
        @DbColumn(name = "cst_id")
        Long id,
        @DbColumn(name = "cst_name")
        String name
) {

    @DbConstructor
    public InnerCustomer {
    }
}
