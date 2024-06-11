package pl.bodzioch.damian.dto;

import java.io.Serializable;
import java.util.List;

public record GetAllDocumentTypesResponse(
        List<DocumentTypeData> types
) implements Serializable {
}
