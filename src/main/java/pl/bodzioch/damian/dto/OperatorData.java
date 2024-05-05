package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.operator.OperatorDto;
import pl.bodzioch.damian.utils.CipherComponent;

import java.io.Serializable;

public record OperatorData(
        String id,
        String name,
        String phoneNumber
) implements Serializable {

    public OperatorData(OperatorDto operator, CipherComponent cipher) {
        this(
                cipher.encryptMessage(operator.id().toString()),
                operator.name(),
                operator.phoneNumber()
        );
    }
}
