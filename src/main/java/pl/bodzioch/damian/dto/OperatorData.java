package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.operator.InnerOperatorDto;
import pl.bodzioch.damian.operator.OperatorDto;
import pl.bodzioch.damian.utils.CipherComponent;

import java.io.Serializable;
import java.util.Optional;

public record OperatorData(
        String id,
        String name,
        String notes
) implements Serializable {

    public OperatorData(OperatorDto operator, CipherComponent cipher) {
        this(
                cipher.encryptMessage(operator.id().toString()),
                operator.name(),
                operator.notes()
        );
    }

    public OperatorData(InnerOperatorDto operator, CipherComponent cipher) {
        this(
                Optional.ofNullable(operator).map(InnerOperatorDto::id).map(id -> cipher.encryptMessage(id.toString())).orElse(null),
                Optional.ofNullable(operator).map(InnerOperatorDto::name).orElse(null),
                null
        );
    }
}
