package pl.bodzioch.damian.coach;

import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbConstructor;
import pl.bodzioch.damian.infrastructure.database.DbId;

public record InnerCoach(
        @DbId
        @DbColumn(name = "coa_id")
        Long id,
        @DbColumn(name = "coa_first_name")
        String firstName,
        @DbColumn(name = "coa_last_name")
        String lastName
) {
    @DbConstructor
    public InnerCoach {
    }
}
