package pl.bodzioch.damian.service_provider;

import com.fasterxml.uuid.Generators;
import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbConstructor;
import pl.bodzioch.damian.infrastructure.database.DbManyToOne;
import pl.bodzioch.damian.service_provider.command_dto.CreateNewServiceProviderCommand;
import pl.bodzioch.damian.service_provider.command_dto.UpdateServiceProviderCommand;
import pl.bodzioch.damian.user.InnerUser;
import pl.bodzioch.damian.user.InnerUserDto;

import java.time.LocalDateTime;
import java.util.UUID;

record ServiceProvider(
        @DbColumn(name = "spr_id")
        Long id,
        @DbColumn(name = "spr_uuid")
        UUID uuid,
        @DbColumn(name = "spr_version")
        Integer version,
        @DbColumn(name = "spr_bur_id")
        Long burId,
        @DbColumn(name = "spr_name")
        String name,
        @DbColumn(name = "spr_nip")
        Long nip,
        @DbColumn(name = "spr_created_at")
        LocalDateTime createdAt,
        @DbColumn(name = "spr_modified_at")
        LocalDateTime modifiedAt,
        @DbColumn(name = "spr_modified_by")
        Long modifiedBy,
        @DbColumn(name = "spr_created_by")
        Long createdBy,
        @DbManyToOne(prefix = "creator")
        InnerUser creator,
        @DbManyToOne(prefix = "modifier")
        InnerUser modifier
) {

    @DbConstructor
    ServiceProvider {
    }

    ServiceProvider(CreateNewServiceProviderCommand command, Long burId) {
        this(
                null, Generators.timeBasedEpochGenerator().generate(), 0, burId, command.name(), command.nip(),
                null, null, null, command.createdBy(), null, null
        );
    }

    ServiceProvider(UpdateServiceProviderCommand command) {
        this(
                command.id(),
                null,
                command.version(),
                null,
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

    ServiceProviderDto toDto() {
        return new ServiceProviderDto(
                id,
                uuid,
                version,
                burId,
                name,
                nip,
                createdAt,
                modifiedAt,
                new InnerUserDto(creator),
                new InnerUserDto(modifier)
        );
    }
}
