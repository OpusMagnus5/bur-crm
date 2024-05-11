package pl.bodzioch.damian.operator;

import com.fasterxml.uuid.Generators;
import pl.bodzioch.damian.infrastructure.database.*;
import pl.bodzioch.damian.operator.command_dto.CreateNewOperatorCommand;
import pl.bodzioch.damian.operator.command_dto.UpdateOperatorCommand;
import pl.bodzioch.damian.program.InnerProgram;
import pl.bodzioch.damian.user.InnerUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

record Operator(
        @DbId
        @DbColumn(name = "opr_id")
        Long id,
        @DbColumn(name = "opr_uuid")
        UUID uuid,
        @DbColumn(name = "opr_version")
        Integer version,
        @DbColumn(name = "opr_name")
        String name,
        @DbColumn(name = "opr_notes")
        String notes,
        @DbColumn(name = "opr_created_at")
        LocalDateTime createdAt,
        @DbColumn(name = "opr_modified_at")
        LocalDateTime modifiedAt,
        @DbColumn(name = "opr_modified_by")
        Long modifiedBy,
        @DbColumn(name = "opr_created_by")
        Long createdBy,
        @DbOneToMany(prefix = "program")
        List<InnerProgram> programs,
        @DbManyToOne(prefix = "creator")
        InnerUser creator,
        @DbManyToOne(prefix = "modifier")
        InnerUser modifier
) {


    @DbConstructor
    Operator {
    }

    Operator(CreateNewOperatorCommand command) {
        this(
                null,
                Generators.timeBasedEpochGenerator().generate(),
                null,
                command.name(),
                command.notes(),
                null,
                null,
                null,
                command.createdBy(),
                null,
                null,
                null
        );
    }

    Operator(UpdateOperatorCommand command) {
        this(
                command.id(),
                null,
                command.version(),
                command.name(),
                command.notes(),
                null,
                null,
                command.modifiedBy(),
                null,
                null,
                null,
                null
        );
    }
}
