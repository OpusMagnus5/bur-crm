package pl.bodzioch.damian.service_provider;

import com.fasterxml.uuid.Generators;
import pl.bodzioch.damian.service_provider.command_dto.CreateNewServiceProviderCommand;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

//Rejestr
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
        Long spr_created_by
) {

    ServiceProvider(CreateNewServiceProviderCommand command, Long burId) {
        this(
                null, Generators.timeBasedEpochGenerator().generate(), 0, burId, command.name(), command.nip(),
                null, null, null, command.createdBy()
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
                spr_modified_at
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
                ((Timestamp) record.get("spr_created_at")).toLocalDateTime(),
                record.get("spr_modified_at") instanceof Timestamp ? ((Timestamp) record.get("usr_modified_at")).toLocalDateTime() : null,
                record.get("usr_modified_by") instanceof Long ? (Long) record.get("usr_modified_by") : null,
                (Long) record.get("usr_created_by")
        );
    }
}
