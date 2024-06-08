package pl.bodzioch.damian.document;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.document.command_dto.AddNewDocumentCommand;
import pl.bodzioch.damian.document.command_dto.AddNewDocumentCommandResult;
import pl.bodzioch.damian.infrastructure.command.CommandHandler;

import java.util.List;

@Component
@RequiredArgsConstructor
class AddNewDocumentCommandHandler implements CommandHandler<AddNewDocumentCommand, AddNewDocumentCommandResult> {

    private final IDocumentReadRepository readRepository;

    @Override
    public Class<AddNewDocumentCommand> commandClass() {
        return AddNewDocumentCommand.class;
    }

    @Override
    public AddNewDocumentCommandResult handle(AddNewDocumentCommand command) {
        List<Document> serviceDocuments = readRepository.getServiceDocuments(command.serviceId());
        Document document = new Document(command, serviceDocuments);

    }
}
