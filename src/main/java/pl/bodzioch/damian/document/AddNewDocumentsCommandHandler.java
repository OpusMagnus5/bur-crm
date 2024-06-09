package pl.bodzioch.damian.document;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.document.command_dto.AddNewDocumentsCommand;
import pl.bodzioch.damian.document.command_dto.AddNewDocumentsCommandData;
import pl.bodzioch.damian.document.command_dto.AddNewDocumentsCommandResult;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;
import pl.bodzioch.damian.utils.MessageResolver;

import java.util.List;

@Component
@RequiredArgsConstructor
class AddNewDocumentsCommandHandler implements CommandHandler<AddNewDocumentsCommand, AddNewDocumentsCommandResult> {

    private final IDocumentReadRepository readRepository;
    private final IDocumentWriteRepository writeRepository;
    private final MessageResolver messageResolver;

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
        //TODO zapis w systemie
        writeRepository.addNewDocuments(documents);
        String message = messageResolver.getMessage("document.addNewDocumentsSuccess");
        return new AddNewDocumentsCommandResult(message);
    }
}
