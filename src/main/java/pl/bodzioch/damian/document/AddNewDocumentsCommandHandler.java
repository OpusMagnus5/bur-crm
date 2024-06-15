package pl.bodzioch.damian.document;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        UUID serviceUuid = serviceDocuments.getFirst().service().uuid();
        saveOnDisc(documents, serviceUuid);

        try {
            writeRepository.addNewDocuments(documents);
        } catch (Exception e) {
            log.error("An error occurred while saving the files in db", e);
            deleteAddedFiles(documents, serviceUuid);
            throw e;
        }

        String message = messageResolver.getMessage("document.addNewDocumentsSuccess");
        return new AddNewDocumentsCommandResult(message);
    }

    private void saveOnDisc(List<Document> documents, UUID serviceUuid) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()){
            documents.stream()
                    .map(item -> save(serviceUuid, item, executor))
                    .forEach(CompletableFuture::join);
        } catch (Exception e) {
            log.error("An error occurred while saving the file for service uuid: " + serviceUuid, e);
            deleteAddedFiles(documents, serviceUuid);
            throw e;
        }
    }

    private void deleteAddedFiles(List<Document> documents, UUID serviceUuid) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()){
            documents.stream()
                    .map(item -> delete(serviceUuid, item.uuid(), executor))
                    .forEach(CompletableFuture::join);
        } catch (Exception e) {
            log.error("An error occurred while deleting the file for service uuid: " + serviceUuid, e);
            throw e;
        }
    }

    private CompletableFuture<Void> save(UUID serviceUuid, Document item, ExecutorService executor) {
        return CompletableFuture.supplyAsync(
                () -> fileManager.saveOnDisc(serviceUuid.toString(), item.uuid().toString(), item.fileData()), executor);
    }

    private CompletableFuture<Void> delete(UUID serviceUuid, UUID documentUuid, ExecutorService executor) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return fileManager.deleteFile(serviceUuid.toString(), documentUuid.toString());
                    } catch (Exception e) {
                        log.warn("An error occurred while deleting the file for service uuid: " + serviceUuid, e);
                        return null;
                    }
                }, executor);
    }
}
