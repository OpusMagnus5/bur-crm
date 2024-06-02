package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.intermediary.InnerIntermediaryDto;
import pl.bodzioch.damian.intermediary.IntermediaryDto;
import pl.bodzioch.damian.utils.CipherComponent;

import java.io.Serializable;
import java.util.Optional;

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

    public IntermediaryData(InnerIntermediaryDto intermediary, CipherComponent cipher) {
        this(
                Optional.ofNullable(intermediary)
                        .map(InnerIntermediaryDto::id)
                        .map(item -> cipher.encryptMessage(item.toString()))
                        .orElse(null),
                Optional.ofNullable(intermediary).map(InnerIntermediaryDto::name).orElse(null),
                null
        );
    }
}
