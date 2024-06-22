package pl.bodzioch.damian.document.command_dto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.bodzioch.damian.document.DocumentType;
import pl.bodzioch.damian.utils.CipherComponent;

import java.io.IOException;
import java.util.Optional;

public record AddNewDocumentsCommandData(
        DocumentType type,
        String fileName,
        String fileExtension,
        Long serviceId,
        Long coachId,
        byte[] fileData,
        Long creatorId
) {

    public AddNewDocumentsCommandData(MultipartFile file, String fileType, String serviceId, String coachId,
                                      CipherComponent cipher) throws IOException {
        this(
                DocumentType.valueOf(fileType),
                file.getOriginalFilename(),
                getFileExtension(file),
                Long.parseLong(cipher.decryptMessage(serviceId)),
                Optional.ofNullable(coachId).filter(StringUtils::isNotBlank).map(cipher::decryptMessage).map(Long::parseLong).orElse(null),
                file.getBytes(),
                cipher.getPrincipalId()
        );
    }

    private static String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        int lastDotIndex = originalFilename.lastIndexOf(".");
        return originalFilename.substring(lastDotIndex + 1);
    }
}
