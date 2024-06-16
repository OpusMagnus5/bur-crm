package pl.bodzioch.damian.infrastructure.file;

import java.util.List;

public record FolderData(
        String folderInZip,
        List<FileData> fileData
) {
}
