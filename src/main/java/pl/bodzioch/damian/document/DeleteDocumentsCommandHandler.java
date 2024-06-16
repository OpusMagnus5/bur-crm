package pl.bodzioch.damian.document;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.document.command_dto.DeleteDocumentsCommand;
import pl.bodzioch.damian.document.command_dto.DeleteDocumentsCommandResult;
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
class DeleteDocumentsCommandHandler implements CommandHandler<DeleteDocumentsCommand, DeleteDocumentsCommandResult> {

    private final IDocumentReadRepository readRepository;
    private final IDocumentWriteRepository writeRepository;
    private final IFileManager fileManager;
    private final MessageResolver messageResolver;

    @Override
    public Class<DeleteDocumentsCommand> commandClass() {
        return DeleteDocumentsCommand.class;
    }

    @Override
    public DeleteDocumentsCommandResult handle(DeleteDocumentsCommand command) {
        List<Document> documents = readRepository.getDocuments(command.ids());
        writeRepository.deleteDocuments(command.ids());
        deleteFiles(documents);
        String message = messageResolver.getMessage("document.deleteDocumentsSuccess");
        return new DeleteDocumentsCommandResult(message);
    }

    private void deleteFiles(List<Document> documents) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()){
            documents.stream()
                    .map(item -> delete(item.service().uuid(), item.uuid(), executor))
                    .forEach(CompletableFuture::join);
        } catch (Exception e) {
            log.error("An error occurred while deleting the file", e);
            throw e;
        }
    }

    private CompletableFuture<Void> delete(UUID serviceUuid, UUID documentUuid, ExecutorService executor) {
        return CompletableFuture.supplyAsync(
                () -> fileManager.deleteFile(serviceUuid.toString(), documentUuid.toString()), executor);
    }
}
