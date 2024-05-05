package pl.bodzioch.damian.operator;

import com.fasterxml.uuid.Generators;
import pl.bodzioch.damian.operator.command_dto.CreateNewOperatorCommand;
import pl.bodzioch.damian.user.InnerUser;
import pl.bodzioch.damian.utils.DbCaster;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

record Operator(

        Long opr_id,
        UUID opr_uuid,
        Integer opr_version,
        String opr_name,
        String opr_notes,
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
                command.notes(),
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
        fields.put("_opr_notes", opr_notes);
        fields.put("_opr_created_by", opr_created_by);
        fields.put("_opr_modified_by", opr_modified_by);
        return fields;
    }

    static List<Operator> fromProperties(Map<String, Object> properties, String cursorName) {
        return DbCaster.fromProperties(Operator::buildServiceProvider, properties, cursorName);
    }

    private static Operator buildServiceProvider(Map<String, Object> record) {
        return new Operator(
                DbCaster.cast(Long.class, record.get("opr_id")),
                DbCaster.cast(UUID.class, record.get("opr_uuid")),
                DbCaster.cast(Integer.class, record.get("opr_version")),
                DbCaster.cast(String.class, record.get("opr_name")),
                DbCaster.cast(String.class, record.get("opr_notes")),
                DbCaster.mapTimestamp(record.get("opr_created_at")),
                DbCaster.mapTimestamp(record.get("opr_modified_at")),
                DbCaster.cast(Long.class, record.get("opr_modified_by")),
                DbCaster.cast(Long.class, record.get("opr_created_by")),
                buildCreator(record),
                buildModifier(record)
        );
    }

    private static InnerUser buildCreator(Map<String, Object> record) {
        return new InnerUser(
                DbCaster.cast(String.class, record.get("creator_usr_first_name")),
                DbCaster.cast(String.class, record.get("creator_usr_last_name"))
        );
    }

    private static InnerUser buildModifier(Map<String, Object> record) {
        return new InnerUser(
                DbCaster.cast(String.class, record.get("modifier_usr_first_name")),
                DbCaster.cast(String.class, record.get("modifier_usr_last_name"))
        );
    }
}
