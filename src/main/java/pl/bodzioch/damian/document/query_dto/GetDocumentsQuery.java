package pl.bodzioch.damian.document.query_dto;

import pl.bodzioch.damian.document.DocumentType;
import pl.bodzioch.damian.infrastructure.query.Query;

import java.util.List;

public record GetDocumentsQuery(
        List<Long> ids,
        DocumentType documentType
) implements Query<GetDocumentsQueryResult> {
}
