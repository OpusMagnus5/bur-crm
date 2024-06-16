package pl.bodzioch.damian.document;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.document.query_dto.GetAllServiceDocumentsQuery;
import pl.bodzioch.damian.document.query_dto.GetAllServiceDocumentsQueryResult;
import pl.bodzioch.damian.infrastructure.file.FileData;
import pl.bodzioch.damian.infrastructure.file.FolderData;
import pl.bodzioch.damian.infrastructure.file.IFileManager;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.utils.MessageResolver;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class GetAllServiceDocumentsQueryHandler implements QueryHandler<GetAllServiceDocumentsQuery, GetAllServiceDocumentsQueryResult> {

    private final IDocumentReadRepository readRepository;
    private final IFileManager fileManager;
    private final MessageResolver messageResolver;

    @Override
    public Class<GetAllServiceDocumentsQuery> queryClass() {
        return GetAllServiceDocumentsQuery.class;
    }

    @Override
    public GetAllServiceDocumentsQueryResult handle(GetAllServiceDocumentsQuery query) {
        List<Document> serviceDocuments = readRepository.getServiceDocuments(query.serviceId());
        List<FolderData> folderData = serviceDocuments.stream()
                .collect(Collectors.groupingBy(Document::type))
                .entrySet().stream()
                .map(entry -> new FolderData(
                        messageResolver.getMessage("document.type.file." + entry.getKey().name()),
                        mapToFileData(entry))
                ).toList();

        byte[] zippedFiles = fileManager.getFilesByFolders(folderData);
        String serviceNumber = serviceDocuments.getFirst().service().number().replaceAll("/", "-");
        String fileName = messageResolver.getMessage("document.type.folder.serviceDocuments");
        return new GetAllServiceDocumentsQueryResult(zippedFiles, fileName + "_" + serviceNumber + IFileManager.ZIP_FILE_EXTENSION);
    }

    private List<FileData> mapToFileData(Map.Entry<DocumentType, List<Document>> entry) {
        return entry.getValue().stream()
                .map(doc -> new FileData(doc.service().uuid().toString(), doc.uuid().toString(), doc.fileName()))
                .toList();
    }
}
