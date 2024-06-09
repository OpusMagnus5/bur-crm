package pl.bodzioch.damian.infrastructure.file;

import com.google.common.io.ByteStreams;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

@Component
class FileManager implements IFileManager {

    private static final String APPLICATION_PATH = System.getProperty("user.dir");
    private static final String DOCUMENTS_PATH = APPLICATION_PATH + File.separator + "documents";
    private static final String GZ_FILE_EXTENSION = ".gz";

    @Override
    public Void saveOnDisc(String path, String fileName, byte[] data) {
        File servicePath = new File(DOCUMENTS_PATH + File.separator + path);
        if (!servicePath.exists()) {
            if (!servicePath.mkdirs()) {
                throw new FileManagerException("Failed to create directory: " + servicePath);
            }
        }

        String completePath = servicePath + File.separator + fileName + GZ_FILE_EXTENSION;
        zipAndSaveFile(completePath, data);
        return null;
    }

    @Override
    public Void deleteFile(String path, String fileName) {
        File file = new File(DOCUMENTS_PATH + File.separator + path + File.separator + fileName + GZ_FILE_EXTENSION);
        if (file.exists()) {
            if (!file.delete()) {
                throw new FileManagerException("Failed to delete file: " + file);
            }
        } else {
            throw new FileManagerException("File not exists: " + file);
        }
        return null;
    }

    private void zipAndSaveFile(String path, byte[] data) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream);
             ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data)){
            ByteStreams.copy(byteArrayInputStream, gzipOutputStream);
        } catch (IOException e) {
            throw new FileManagerException("An error occurred while saving the file", e);
        }
    }
}
