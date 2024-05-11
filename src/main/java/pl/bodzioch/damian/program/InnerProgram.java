package pl.bodzioch.damian.program;

import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbConstructor;
import pl.bodzioch.damian.infrastructure.database.DbId;

public record InnerProgram(
        @DbId
        @DbColumn(name = "prg_id")
        Long id,
        @DbColumn(name = "prg_name")
        String name
)
{
        @DbConstructor
        public InnerProgram {
        }
}
