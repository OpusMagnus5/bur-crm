package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.document.InnerDocumentDto;
import pl.bodzioch.damian.utils.CipherComponent;
import pl.bodzioch.damian.utils.MessageResolver;

import java.time.LocalDateTime;
import java.util.Optional;

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
                Optional.ofNullable(document.coachId()).map(String::valueOf).map(cipher::encryptMessage).orElse(null),
                new DocumentTypeData(document.type(), messageResolver),
                document.fileName(),
                document.fileExtension(),
                document.createdAt(),
                new UserListData(document.creator(), cipher)
        );
    }
}
