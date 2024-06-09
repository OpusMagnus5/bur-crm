package pl.bodzioch.damian.infrastructure.file;

public interface IFileManager {
    Void saveOnDisc(String path, String fileName, byte[] data);

    Void deleteFile(String path, String fileName);
}
