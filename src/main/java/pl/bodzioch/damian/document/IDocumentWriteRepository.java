package pl.bodzioch.damian.document;

import jakarta.transaction.Transactional;

import java.util.List;

interface IDocumentWriteRepository {
    @Transactional(Transactional.TxType.REQUIRED)
    void addNewDocuments(List<Document> documents);

    @Transactional(Transactional.TxType.REQUIRED)
    void deleteDocuments(List<Long> documents);
}
