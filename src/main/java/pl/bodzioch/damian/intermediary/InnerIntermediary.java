package pl.bodzioch.damian.intermediary;

import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbConstructor;
import pl.bodzioch.damian.infrastructure.database.DbId;

public record InnerIntermediary(
        @DbId
        @DbColumn(name = "itr_id")
        Long id,
        @DbColumn(name = "itr_name")
        String name
) {
    @DbConstructor
    public InnerIntermediary {
    }
}
