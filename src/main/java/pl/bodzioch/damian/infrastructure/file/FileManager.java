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

    @Override
    public Void saveOnDisc(String path, String fileName, byte[] data) {
        File servicePath = new File(DOCUMENTS_PATH + File.separator + path);
        if (!servicePath.exists()) {
            boolean directoryCreated = servicePath.mkdirs();
            if (!directoryCreated) {
                throw new FileManagerException("Failed to create directory: " + servicePath);
            }
        }

        String completePath = servicePath + File.separator + fileName + ".gz";
        zipAndSaveFile(completePath, data);
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
