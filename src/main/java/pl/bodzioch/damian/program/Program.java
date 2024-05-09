package pl.bodzioch.damian.program;

import pl.bodzioch.damian.infrastructure.database.DbColumn;
import pl.bodzioch.damian.infrastructure.database.DbConstructor;
import pl.bodzioch.damian.infrastructure.database.DbManyToOne;
import pl.bodzioch.damian.operator.InnerOperator;
import pl.bodzioch.damian.user.InnerUser;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record Program(
        @DbColumn(name = "prg_id")
        Long prg_id,
        @DbColumn(name = "prg_uuid")
        UUID prg_uuid,
        @DbColumn(name = "prg_version")
        Integer prg_version,
        @DbColumn(name = "prg_name")
        String prg_name,
        @DbColumn(name = "prg_operator_id")
        Long prg_operator_id,
        @DbColumn(name = "prg_created_at")
        LocalDateTime prg_created_at,
        @DbColumn(name = "prg_modified_at")
        LocalDateTime prg_modified_at,
        @DbColumn(name = "prg_created_by")
        Long prg_created_by,
        @DbColumn(name = "prg_modified_by")
        Long prg_modified_by,
        @DbManyToOne(prefix = "operator")
        InnerOperator operator,
        @DbManyToOne(prefix = "creator")
        InnerUser creator,
        @DbManyToOne(prefix = "modifier")
        InnerUser modifier
) {

    @DbConstructor
    public Program {
    }

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
