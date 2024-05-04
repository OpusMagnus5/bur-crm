package pl.bodzioch.damian.service_provider;

import com.fasterxml.uuid.Generators;
import pl.bodzioch.damian.service_provider.command_dto.CreateNewServiceProviderCommand;
import pl.bodzioch.damian.service_provider.command_dto.UpdateServiceProviderCommand;
import pl.bodzioch.damian.user.InnerUser;
import pl.bodzioch.damian.user.InnerUserDto;
import pl.bodzioch.damian.utils.DbCaster;

import java.time.LocalDateTime;
import java.util.*;

record ServiceProvider(

        Long spr_id,
        UUID spr_uuid,
        Integer spr_version,
        Long spr_bur_id,
        String spr_name,
        Long spr_nip,
        LocalDateTime spr_created_at,
        LocalDateTime spr_modified_at,
        Long spr_modified_by,
        Long spr_created_by,
        InnerUser creator,
        InnerUser modifier
) {

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
                spr_id,
                spr_uuid,
                spr_version,
                spr_bur_id,
                spr_name,
                spr_nip,
                spr_created_at,
                spr_modified_at,
                Optional.ofNullable(creator).map(InnerUserDto::new).orElse(null),
                Optional.ofNullable(modifier).map(InnerUserDto::new).orElse(null)
        );
    }

    Map<String, Object> getPropertySource() {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put("_spr_id", spr_id);
        fields.put("_spr_uuid", spr_uuid);
        fields.put("_spr_version", spr_version);
        fields.put("_spr_bur_id", spr_bur_id);
        fields.put("_spr_name", spr_name);
        fields.put("_spr_nip", spr_nip);
        fields.put("_spr_created_by", spr_created_by);
        fields.put("_spr_modified_by", spr_modified_by);
        return fields;
    }

    static List<ServiceProvider> fromProperties(Map<String, Object> properties, String cursorName) {
        return DbCaster.fromProperties(ServiceProvider::buildServiceProvider, properties, cursorName);
    }

    private static ServiceProvider buildServiceProvider(Map<String, Object> record) {
        return new ServiceProvider(
                DbCaster.cast(Long.class, record.get("spr_id")),
                DbCaster.cast(UUID.class, record.get("spr_uuid")),
                DbCaster.cast(Integer.class, record.get("spr_version")),
                DbCaster.cast(Long.class, record.get("spr_bur_id")),
                DbCaster.cast(String.class, record.get("spr_name")),
                DbCaster.cast(Long.class, record.get("spr_nip")),
                DbCaster.mapTimestamp(record.get("spr_created_at")),
                DbCaster.mapTimestamp(record.get("spr_modified_at")),
                DbCaster.cast(Long.class, record.get("spr_modified_by")),
                DbCaster.cast(Long.class, record.get("spr_created_by")),
                buildInnerUser(record, "creator"),
                buildInnerUser(record, "modifier")
        );
    }

    private static InnerUser buildInnerUser(Map<String, Object> record, String userKind) {
        return new InnerUser(
                DbCaster.cast(String.class, record.get(userKind + "_usr_first_name")),
                DbCaster.cast(String.class, record.get(userKind + "_usr_last_name"))
        );
    }
}
