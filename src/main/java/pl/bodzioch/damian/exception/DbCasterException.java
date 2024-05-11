package pl.bodzioch.damian.exception;

public class DbCasterException extends RuntimeException {

    public DbCasterException(String message) {
        super(message);
    }

    public DbCasterException(String message, Throwable cause) {
        super(message, cause);
    }
}
