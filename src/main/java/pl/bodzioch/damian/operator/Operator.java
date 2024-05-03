package pl.bodzioch.damian.operator;

import com.fasterxml.uuid.Generators;
import pl.bodzioch.damian.operator.command_dto.CreateNewOperatorCommand;
import pl.bodzioch.damian.user.InnerUser;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

record Operator(

        Long opr_id,
        UUID opr_uuid,
        Integer opr_version,
        String opr_name,
        String opr_phone_number,
        LocalDateTime opr_created_at,
        LocalDateTime opr_modified_at,
        Long opr_modified_by,
        Long opr_created_by,
        InnerUser creator,
        InnerUser modifier
) {

    Operator(CreateNewOperatorCommand command) {
        this(
                null,
                Generators.timeBasedEpochGenerator().generate(),
                null,
                command.name(),
                command.phoneNumber(),
                null,
                null,
                null,
                command.createdBy(),
                null,
                null
        );
    }

    Map<String, Object> getPropertySource() {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put("_opr_id", opr_id);
        fields.put("_opr_uuid", opr_uuid);
        fields.put("_opr_version", opr_version);
        fields.put("_opr_name", opr_name);
        fields.put("_opr_phone_number", opr_phone_number);
        fields.put("_opr_created_by", opr_created_by);
        fields.put("_opr_modified_by", opr_modified_by);
        return fields;
    }
}
