package pl.bodzioch.damian.customer;

import com.fasterxml.uuid.Generators;
import pl.bodzioch.damian.customer.command_dto.CreateNewCustomerCommand;
import pl.bodzioch.damian.customer.command_dto.UpdateCustomerCommand;
import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbConstructor;
import pl.bodzioch.damian.infrastructure.database.DbId;
import pl.bodzioch.damian.infrastructure.database.DbManyToOne;
import pl.bodzioch.damian.user.InnerUser;

import java.time.LocalDateTime;
import java.util.UUID;

record Customer(
        @DbId
        @DbColumn(name = "cst_id")
        Long id,
        @DbColumn(name = "cst_uuid")
        UUID uuid,
        @DbColumn(name = "cst_version")
        Integer version,
        @DbColumn(name = "cst_name")
        String name,
        @DbColumn(name = "cst_nip")
        Long nip,
        @DbColumn(name = "cst_created_at")
        LocalDateTime createdAt,
        @DbColumn(name = "cst_modified_at")
        LocalDateTime modifiedAt,
        @DbColumn(name = "cst_modified_by")
        Long modifiedBy,
        @DbColumn(name = "cst_created_by")
        Long createdBy,
        @DbManyToOne(prefix = "creator")
        InnerUser creator,
        @DbManyToOne(prefix = "modifier")
        InnerUser modifier
) {

    @DbConstructor
    Customer {
    }

    Customer(CreateNewCustomerCommand command) {
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

    Customer(UpdateCustomerCommand command) {
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
