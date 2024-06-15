package pl.bodzioch.damian.infrastructure.file;

import com.google.common.io.ByteStreams;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.AbstractMap;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    @Override
    public byte[] getFiles(List<FileData> files) {
        List<AbstractMap.SimpleEntry<FileData, File>> filesData = files.stream()
                .map(item -> new AbstractMap.SimpleEntry<>(item, new File(
                        DOCUMENTS_PATH +
                                File.separator +
                                item.path() +
                                File.separator +
                                item.name() +
                                GZ_FILE_EXTENSION))
                ).toList();

        return getFilesAsZip(filesData);
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

    private byte[] getFilesAsZip(List<AbstractMap.SimpleEntry<FileData, File>> filesData) {
        ByteArrayOutputStream zipByteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(zipByteArrayOutputStream);
        for (AbstractMap.SimpleEntry<FileData, File> fileData : filesData) {
            try (FileInputStream fileInputStream = new FileInputStream(fileData.getValue());
                 GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream)) {

                ZipEntry zipEntry = new ZipEntry(fileData.getKey().nameInZip());
                zipOut.putNextEntry(zipEntry);
                ByteStreams.copy(gzipInputStream, zipOut);

            } catch (IOException e) {
                throw new FileManagerException("An error occurred while get files as zip", e);
            }
        }
        return zipByteArrayOutputStream.toByteArray();
    }
}
