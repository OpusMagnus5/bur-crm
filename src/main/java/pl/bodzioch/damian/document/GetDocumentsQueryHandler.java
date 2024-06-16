package pl.bodzioch.damian.document;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.document.query_dto.GetDocumentsQuery;
import pl.bodzioch.damian.document.query_dto.GetDocumentsQueryResult;
import pl.bodzioch.damian.infrastructure.file.FileData;
import pl.bodzioch.damian.infrastructure.file.IFileManager;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.utils.MessageResolver;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetDocumentsQueryHandler implements QueryHandler<GetDocumentsQuery, GetDocumentsQueryResult> {

    private final IDocumentReadRepository readRepository;
    private final IFileManager fileManager;
    private final MessageResolver messageResolver;

    @Override
    public Class<GetDocumentsQuery> queryClass() {
        return GetDocumentsQuery.class;
    }

    @Override
    public GetDocumentsQueryResult handle(GetDocumentsQuery query) {
        List<Document> documents = readRepository.getDocuments(query.ids());
        List<FileData> filesData = documents.stream()
                .map(item -> new FileData(item.service().uuid().toString(), item.uuid().toString(), item.fileName()))
                .toList();
        byte[] zippedFiles = fileManager.getFiles(filesData);
        DocumentType documentType = documents.getFirst().type();
        String fileType = messageResolver.getMessage("document.type.file." + documentType.name());
        String serviceNumber = documents.getFirst().service().number().replaceAll("/", "-");
        return new GetDocumentsQueryResult(zippedFiles, fileType + "_" + serviceNumber + ".zip");
    }
}
