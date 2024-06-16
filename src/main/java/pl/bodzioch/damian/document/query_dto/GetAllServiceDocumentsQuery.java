package pl.bodzioch.damian.document.query_dto;

import pl.bodzioch.damian.infrastructure.query.Query;

public record GetAllServiceDocumentsQuery(
        Long serviceId
) implements Query<GetAllServiceDocumentsQueryResult> {
}
