package pl.bodzioch.damian.program;

import pl.bodzioch.damian.infrastructure.database.DbColumn;

public record InnerProgram(
        @DbColumn(name = "")
        String name
) {

}
