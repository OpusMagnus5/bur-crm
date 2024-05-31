package pl.bodzioch.damian.service_provider;

import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbConstructor;
import pl.bodzioch.damian.infrastructure.database.DbId;

public record InnerServiceProvider(
        @DbId
        @DbColumn(name = "spr_id")
        Long id,
        @DbColumn(name = "spr_name")
        String name
) {
    @DbConstructor
    public InnerServiceProvider {
    }
}
