package pl.bodzioch.damian.infrastructure.file;

public class FileManagerException extends RuntimeException {
    FileManagerException(String message) {
        super(message);
    }

    FileManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
