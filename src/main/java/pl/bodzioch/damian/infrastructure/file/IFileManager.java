package pl.bodzioch.damian.infrastructure.file;

import java.util.List;

public interface IFileManager {
    Void saveOnDisc(String path, String fileName, byte[] data);

    Void deleteFile(String path, String fileName);

    byte[] getFiles(List<FileData> files);
}
