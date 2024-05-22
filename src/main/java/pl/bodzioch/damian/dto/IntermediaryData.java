package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.intermediary.IntermediaryDto;
import pl.bodzioch.damian.utils.CipherComponent;

import java.io.Serializable;

public record IntermediaryData(
        String id,
        String name,
        String nip
) implements Serializable {

    public IntermediaryData(IntermediaryDto intermediary, CipherComponent cipher) {
        this(
                cipher.encryptMessage(intermediary.id().toString()),
                intermediary.name(),
                intermediary.nip().toString()
        );
    }
}
