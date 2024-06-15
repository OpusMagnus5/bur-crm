package pl.bodzioch.damian.document;

import jakarta.transaction.Transactional;

import java.util.List;

interface IDocumentReadRepository {
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    List<Document> getServiceDocuments(Long serviceId, DocumentType documentType, Long coachId);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    List<Document> getDocuments(List<Long> documentIds);
}
