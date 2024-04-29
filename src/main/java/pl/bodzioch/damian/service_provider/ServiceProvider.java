package pl.bodzioch.damian.service_provider;

import com.fasterxml.uuid.Generators;
import pl.bodzioch.damian.service_provider.command_dto.CreateNewServiceProviderCommand;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

}
