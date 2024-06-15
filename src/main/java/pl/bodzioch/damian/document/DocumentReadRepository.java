package pl.bodzioch.damian.document;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.DbCaster;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
class DocumentReadRepository implements IDocumentReadRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall getAllForServiceProc;
    private final SimpleJdbcCall getDocumentsByIdsProc;

    DocumentReadRepository(IJdbcCaller jdbcCaller) {
        this.jdbcCaller = jdbcCaller;
        this.getAllForServiceProc = this.jdbcCaller.buildSimpleJdbcCall("document_get_all_for_service");
        this.getDocumentsByIdsProc = this.jdbcCaller.buildSimpleJdbcCall("document_get_by_ids");
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public List<Document> getServiceDocuments(Long serviceId, DocumentType documentType, Long coachId) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("_doc_service_id", serviceId);
        properties.put("_doc_type", documentType.name());
        properties.put("_doc_coach_id", coachId);

        Map<String, Object> result = this.jdbcCaller.call(this.getAllForServiceProc, properties);
        List<Document> documents = DbCaster.fromProperties(result, Document.class);
        return documents.stream()
                .filter(item -> item.type() != null)
                .findAny()
                .map(List::of)
                .orElse(List.of(documents.getFirst()));
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public List<Document> getDocuments(List<Long> documentIds) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("_doc_ids", documentIds.toArray(Long[]::new));

        Map<String, Object> result = this.jdbcCaller.call(this.getDocumentsByIdsProc, properties);
        return DbCaster.fromProperties(result, Document.class);
    }
}
