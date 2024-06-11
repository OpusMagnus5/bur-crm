package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.document.InnerDocumentDto;
import pl.bodzioch.damian.utils.CipherComponent;
import pl.bodzioch.damian.utils.MessageResolver;

import java.time.LocalDateTime;

public record DocumentData(
        String id,
        String coachId,
        DocumentTypeData type,
        String fileName,
        String fileExtension,
        LocalDateTime createdAt,
        UserListData creator
) {

    public DocumentData(InnerDocumentDto document, CipherComponent cipher, MessageResolver messageResolver) {
        this(
                cipher.encryptMessage(document.id().toString()),
                cipher.encryptMessage(document.coachId().toString()),
                new DocumentTypeData(document.type(), messageResolver),
                document.fileName(),
                document.fileExtension(),
                document.createdAt(),
                new UserListData(document.creator(), cipher)
        );
    }
}
