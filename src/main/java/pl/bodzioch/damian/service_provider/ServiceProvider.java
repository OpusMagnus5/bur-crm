package pl.bodzioch.damian.service_provider;

import com.fasterxml.uuid.Generators;
import pl.bodzioch.damian.service_provider.command_dto.CreateNewServiceProviderCommand;
import pl.bodzioch.damian.service_provider.command_dto.UpdateServiceProviderCommand;
import pl.bodzioch.damian.user.InnerUser;
import pl.bodzioch.damian.user.InnerUserDto;

import java.sql.Timestamp;
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

    @SuppressWarnings("unchecked")
    static List<ServiceProvider> fromProperties(Map<String, Object> properties, String cursorName) {
        ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) properties.get(cursorName);
        ArrayList<ServiceProvider> serviceProviders = new ArrayList<>();
        for (Map<String, Object> record : list) {
            ServiceProvider serviceProvider = buildServiceProvider(record);
            serviceProviders.add(serviceProvider);
        }
        return serviceProviders;
    }

    private static ServiceProvider buildServiceProvider(Map<String, Object> record) {
        return new ServiceProvider(
                (Long) record.get("spr_id"),
                (UUID) record.get("spr_uuid"),
                (Integer) record.get("spr_version"),
                (Long) record.get("spr_bur_id"),
                (String) record.get("spr_name"),
                (Long) record.get("spr_nip"),
                record.get("spr_created_at") instanceof Timestamp ? ((Timestamp) record.get("spr_created_at")).toLocalDateTime() : null,
                record.get("spr_modified_at") instanceof Timestamp ? ((Timestamp) record.get("usr_modified_at")).toLocalDateTime() : null,
                record.get("usr_modified_by") instanceof Long ? (Long) record.get("usr_modified_by") : null,
                (Long) record.get("usr_created_by"),
                buildInnerUser(record, "creator"),
                buildInnerUser(record, "modifier")
        );
    }

    private static InnerUser buildInnerUser(Map<String, Object> record, String userKind) {
        return new InnerUser(
                (String) record.get(userKind + "_usr_first_name"),
                (String) record.get(userKind + "_usr_last_name")
        );
    }
}
