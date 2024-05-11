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
        Long prg_id,
        @DbColumn(name = "prg_uuid")
        UUID prg_uuid,
        @DbColumn(name = "prg_version")
        Integer prg_version,
        @DbColumn(name = "prg_name")
        String prg_name,
        @DbColumn(name = "prg_operator_id")
        Long prg_operator_id,
        @DbColumn(name = "prg_created_at")
        LocalDateTime prg_created_at,
        @DbColumn(name = "prg_modified_at")
        LocalDateTime prg_modified_at,
        @DbColumn(name = "prg_created_by")
        Long prg_created_by,
        @DbColumn(name = "prg_modified_by")
        Long prg_modified_by,
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
