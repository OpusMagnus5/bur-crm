package pl.bodzioch.damian.document;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.document.command_dto.AddNewDocumentsCommand;
import pl.bodzioch.damian.document.command_dto.AddNewDocumentsCommandData;
import pl.bodzioch.damian.document.command_dto.AddNewDocumentsCommandResult;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.infrastructure.file.IFileManager;
import pl.bodzioch.damian.utils.MessageResolver;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
class AddNewDocumentsCommandHandler implements CommandHandler<AddNewDocumentsCommand, AddNewDocumentsCommandResult> {

    private final IDocumentReadRepository readRepository;
    private final IDocumentWriteRepository writeRepository;
    private final MessageResolver messageResolver;
    private final IFileManager fileManager;

    @Override
    public Class<AddNewDocumentsCommand> commandClass() {
        return AddNewDocumentsCommand.class;
    }

    @Override
    public AddNewDocumentsCommandResult handle(AddNewDocumentsCommand command) {
        List<AddNewDocumentsCommandData> documentsToAdd = command.documents();
        Long serviceId = documentsToAdd.getFirst().serviceId();
        DocumentType documentType = documentsToAdd.getFirst().type();
        Long coachId = documentsToAdd.getFirst().coachId();

        List<Document> serviceDocuments = readRepository.getServiceDocuments(serviceId, documentType, coachId);
        List<Document> documents = Document.newDocuments(documentsToAdd, serviceDocuments);

        saveOnDisc(documents, serviceDocuments.getFirst().uuid());
        writeRepository.addNewDocuments(documents);

        String message = messageResolver.getMessage("document.addNewDocumentsSuccess");
        return new AddNewDocumentsCommandResult(message);
    }

    private void saveOnDisc(List<Document> documents, UUID serviceUuid) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()){
            documents.stream()
                    .map(item -> save(serviceUuid, item, executor))
                    .forEach(CompletableFuture::join);
        }
    }

    private CompletableFuture<Void> save(UUID serviceUuid, Document item, ExecutorService executor) {
        return CompletableFuture.supplyAsync(
                () -> fileManager.saveOnDisc(serviceUuid.toString(), item.uuid().toString(), item.fileData()), executor);
    }
}
