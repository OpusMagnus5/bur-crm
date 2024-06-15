package pl.bodzioch.damian.document.query_dto;

import pl.bodzioch.damian.infrastructure.query.QueryResult;

public record GetDocumentsQueryResult(
        byte[] zipFileData,
        String name
) implements QueryResult {
}
