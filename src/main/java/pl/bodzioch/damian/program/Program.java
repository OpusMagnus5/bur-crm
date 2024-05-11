package pl.bodzioch.damian.program;

import com.fasterxml.uuid.Generators;
import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbConstructor;
import pl.bodzioch.damian.infrastructure.database.DbId;
import pl.bodzioch.damian.infrastructure.database.DbManyToOne;
import pl.bodzioch.damian.operator.InnerOperator;
import pl.bodzioch.damian.program.command_dto.CreateNewProgramCommand;
import pl.bodzioch.damian.user.InnerUser;

import java.time.LocalDateTime;
import java.util.UUID;

record Program(
        @DbId
        @DbColumn(name = "prg_id")
        Long id,
        @DbColumn(name = "prg_uuid")
        UUID uuid,
        @DbColumn(name = "prg_version")
        Integer version,
        @DbColumn(name = "prg_name")
        String name,
        @DbColumn(name = "prg_operator_id")
        Long operatorId,
        @DbColumn(name = "prg_created_at")
        LocalDateTime createdAt,
        @DbColumn(name = "prg_modified_at")
        LocalDateTime modifiedAt,
        @DbColumn(name = "prg_created_by")
        Long createdBy,
        @DbColumn(name = "prg_modified_by")
        Long modifiedBy,
        @DbManyToOne(prefix = "operator")
        InnerOperator operator,
        @DbManyToOne(prefix = "creator")
        InnerUser creator,
        @DbManyToOne(prefix = "modifier")
        InnerUser modifier
) {

    @DbConstructor
    public Program {
    }

    Program(CreateNewProgramCommand command) {
        this(
                null,
                Generators.timeBasedEpochGenerator().generate(),
                null,
                command.name(),
                command.operatorId(),
                null,
                null,
                command.createdBy(),
                null,
                null,
                null,
                null
        );
    }
}
