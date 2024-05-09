package pl.bodzioch.damian.program;

import pl.bodzioch.damian.operator.InnerOperator;
import pl.bodzioch.damian.user.InnerUser;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record Program(
        Long prg_id,
        UUID prg_uuid,
        Integer prg_version,
        String prg_name,
        Long prg_operator_id,
        LocalDateTime prg_created_at,
        LocalDateTime prg_modified_at,
        Long prg_created_by,
        Long prg_modified_by,
        InnerOperator operator,
        InnerUser creator,
        InnerUser modifier
) {

    Map<String, Object> getPropertySource() {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put("_prg_id", prg_id);
        fields.put("_prg_uuid", prg_uuid);
        fields.put("_prg_version", prg_version);
        fields.put("_prg_name", prg_name);
        fields.put("_prg_operator_id", prg_operator_id);
        fields.put("_prg_created_by", prg_created_by);
        fields.put("_prg_modified_by", prg_modified_by);
        return fields;
    }
}
