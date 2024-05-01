package pl.bodzioch.damian.exception;

import com.fasterxml.uuid.Generators;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import pl.bodzioch.damian.value_object.ErrorData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class AppException extends RuntimeException {

    public static final String REQUEST_ID_MDC_PARAM = "requestId";

    private final UUID errorId = Generators.timeBasedEpochGenerator().generate();
    private final HttpStatus httpStatus;
    private final LocalDateTime occurredAt = LocalDateTime.now();
    private final List<ErrorData> errors;

    public AppException(String message, HttpStatus httpStatus, List<ErrorData> errors) {
        super(message);
        this.httpStatus = httpStatus;
        this.errors = errors;
    }

    public AppException(String message) {
        super(message);
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        errors = List.of(ErrorData.getGeneralErrorData());
    }

    public static AppException getGeneralError() {
        return new AppException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                List.of(ErrorData.getGeneralErrorData())
        );
    }

    public static AppException getGeneralError(Throwable cause) {
        return new AppException(cause);
    }

    private AppException(Throwable cause) {
        super(cause);
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        errors = List.of(ErrorData.getGeneralErrorData());
    }
}
