package pl.bodzioch.damian.document.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

import java.util.List;

public record GetDocumentsQuery(
        List<Long> ids
) implements Query<GetDocumentsQueryResult> {
}
