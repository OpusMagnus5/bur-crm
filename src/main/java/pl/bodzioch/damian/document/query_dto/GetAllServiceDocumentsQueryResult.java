package pl.bodzioch.damian.document.query_dto;

import pl.bodzioch.damian.infrastructure.query.QueryResult;

public record GetAllServiceDocumentsQueryResult(
        byte[] zipData,
        String fileName
) implements QueryResult {
}
