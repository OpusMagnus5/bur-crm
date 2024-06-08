package pl.bodzioch.damian.document;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.DbCaster;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import java.util.List;
import java.util.Map;

@Repository
class DocumentReadRepository implements IDocumentReadRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall getAllForServiceProc;

    DocumentReadRepository(IJdbcCaller jdbcCaller) {
        this.jdbcCaller = jdbcCaller;
        this.getAllForServiceProc = this.jdbcCaller.buildSimpleJdbcCall("document_get_all_for_service");
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public List<Document> getServiceDocuments(Long serviceId) {
        Map<String, Object> properties = Map.of("_doc_service_id", serviceId);
        Map<String, Object> result = this.jdbcCaller.call(this.getAllForServiceProc, properties);
        return DbCaster.fromProperties(result, Document.class);
    }
}
