package pl.bodzioch.damian.coach;

import com.fasterxml.uuid.Generators;
import pl.bodzioch.damian.coach.command_dto.CreateNewCoachCommand;
import pl.bodzioch.damian.coach.command_dto.UpdateCoachCommand;
import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbConstructor;
import pl.bodzioch.damian.infrastructure.database.DbId;
import pl.bodzioch.damian.infrastructure.database.DbManyToOne;
import pl.bodzioch.damian.user.InnerUser;

import java.time.LocalDateTime;
import java.util.UUID;

record Coach(
        @DbId
        @DbColumn(name = "coa_id")
        Long id,
        @DbColumn(name = "coa_uuid")
        UUID uuid,
        @DbColumn(name = "coa_version")
        Integer version,
        @DbColumn(name = "coa_first_name")
        String firstName,
        @DbColumn(name = "coa_last_name")
        String lastName,
        @DbColumn(name = "coa_pesel")
        String pesel,
        @DbColumn(name = "coa_created_at")
        LocalDateTime createdAt,
        @DbColumn(name = "coa_modified_at")
        LocalDateTime modifiedAt,
        @DbColumn(name = "coa_modified_by")
        Long modifiedBy,
        @DbColumn(name = "coa_created_by")
        Long createdBy,
        @DbManyToOne(prefix = "creator")
        InnerUser creator,
        @DbManyToOne(prefix = "modifier")
        InnerUser modifier
) {
    @DbConstructor
    Coach {
    }

    Coach(CreateNewCoachCommand command) {
            this(
                    null,
                    Generators.timeBasedEpochGenerator().generate(),
                    null,
                    command.firstName(),
                    command.lastName(),
                    command.pesel(),
                    null,
                    null,
                    null,
                    command.createdBy(),
                    null,
                    null
            );
    }

    Coach(UpdateCoachCommand command) {
        this(
                command.id(),
                null,
                command.version(),
                command.firstName(),
                command.lastName(),
                command.pesel(),
                null,
                null,
                command.modifiedBy(),
                null,
                null,
                null
        );
    }
}
