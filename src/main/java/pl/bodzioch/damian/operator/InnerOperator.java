package pl.bodzioch.damian.operator;

import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbId;

public record InnerOperator(
        @DbId
        @DbColumn(name = "opr_id")
        Long id,
        @DbColumn(name = "opr_name")
        String name
) {
}
