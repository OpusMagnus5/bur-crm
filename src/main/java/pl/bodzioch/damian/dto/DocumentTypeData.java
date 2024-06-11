package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.document.DocumentType;
import pl.bodzioch.damian.utils.MessageResolver;

import java.io.Serializable;

public record DocumentTypeData(
        String value,
        String name
) implements Serializable {

    public DocumentTypeData(DocumentType type, MessageResolver messageResolver) {
        this(
                type.name(),
                messageResolver.getMessage("document.type." + type.name())
        );
    }
}
