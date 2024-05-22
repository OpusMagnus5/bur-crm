package pl.bodzioch.damian.intermediary;

import com.fasterxml.uuid.Generators;
import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbConstructor;
import pl.bodzioch.damian.infrastructure.database.DbId;
import pl.bodzioch.damian.infrastructure.database.DbManyToOne;
import pl.bodzioch.damian.intermediary.command_dto.CreateNewIntermediaryCommand;
import pl.bodzioch.damian.intermediary.command_dto.UpdateIntermediaryCommand;
import pl.bodzioch.damian.user.InnerUser;

import java.time.LocalDateTime;
import java.util.UUID;

record Intermediary(
        @DbId
        @DbColumn(name = "itr_id")
        Long id,
        @DbColumn(name = "itr_uuid")
        UUID uuid,
        @DbColumn(name = "itr_version")
        Integer version,
        @DbColumn(name = "itr_name")
        String name,
        @DbColumn(name = "itr_nip")
        Long nip,
        @DbColumn(name = "itr_created_at")
        LocalDateTime createdAt,
        @DbColumn(name = "itr_modified_at")
        LocalDateTime modifiedAt,
        @DbColumn(name = "itr_modified_by")
        Long modifiedBy,
        @DbColumn(name = "itr_created_by")
        Long createdBy,
        @DbManyToOne(prefix = "creator")
        InnerUser creator,
        @DbManyToOne(prefix = "modifier")
        InnerUser modifier
) {

    @DbConstructor
    Intermediary {
    }

    Intermediary(CreateNewIntermediaryCommand command) {
        this(
                null,
                Generators.timeBasedEpochGenerator().generate(),
                null,
                command.name(),
                command.nip(),
                null,
                null,
                null,
                command.createdBy(),
                null,
                null
        );
    }

    Intermediary(UpdateIntermediaryCommand command) {
        this(
                command.id(),
                null,
                command.version(),
                command.name(),
                command.nip(),
                null,
                null,
                command.modifiedBy(),
                null,
                null,
                null
        );
    }
}
