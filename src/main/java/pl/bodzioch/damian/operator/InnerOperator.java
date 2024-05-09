package pl.bodzioch.damian.operator;

import pl.bodzioch.damian.infrastructure.database.DbColumn;

public record InnerOperator(
        @DbColumn(name = "opr_name")
        String name
) {
}
