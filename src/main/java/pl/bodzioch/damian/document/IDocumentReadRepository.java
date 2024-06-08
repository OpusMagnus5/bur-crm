package pl.bodzioch.damian.document;

import jakarta.transaction.Transactional;

import java.util.List;

interface IDocumentReadRepository {
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    List<Document> getServiceDocuments(Long serviceId);
}
