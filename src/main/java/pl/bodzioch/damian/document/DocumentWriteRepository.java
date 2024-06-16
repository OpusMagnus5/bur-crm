package pl.bodzioch.damian.document;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import java.sql.Types;
import java.util.List;
import java.util.Map;

import static pl.bodzioch.damian.infrastructure.database.CustomTypes.DOCUMENT;

@Repository
class DocumentWriteRepository implements IDocumentWriteRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall addNewDocumentsProc;
    private final SimpleJdbcCall deleteDocumentsProc;

    DocumentWriteRepository(IJdbcCaller jdbcCaller) {
        this.jdbcCaller = jdbcCaller;
        this.addNewDocumentsProc = this.jdbcCaller.buildSimpleJdbcCall("document_add_new");
        this.deleteDocumentsProc = this.jdbcCaller.buildSimpleJdbcCall("document_delete_documents");
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void addNewDocuments(List<Document> documents) {
        String customTypesParameter = jdbcCaller.getArrayCustomTypesParameter(DOCUMENT, documents);
        Map<String, Object> properties = Map.of(DOCUMENT.asArrayParamName(), customTypesParameter);
        this.addNewDocumentsProc.declareParameters(new SqlParameter(DOCUMENT.asArrayParamName(), Types.OTHER));
        this.jdbcCaller.call(this.addNewDocumentsProc, properties);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteDocuments(List<Long> documents) {
        Map<String, Object> properties = Map.of("_doc_ids", documents.toArray(Long[]::new));
        this.jdbcCaller.call(this.deleteDocumentsProc, properties);
    }
}
