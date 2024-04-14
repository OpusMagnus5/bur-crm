package pl.bodzioch.damian.valueobject;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public record ErrorData(
        String errorCode,
        String errorSource,
        List<String> parameters,
        LocalDateTime errorTime

) {
    public static final String GENERAL_ERROR_CODE = "error.server.general";

    public ErrorData(String errorCode, String errorSource, List<String> parameters) {
        this(
                errorCode,
                errorSource,
                parameters,
                LocalDateTime.now()
        );
    }

    public static ErrorData getGeneralErrorData() {
        return new ErrorData(
                GENERAL_ERROR_CODE,
                null,
                Collections.emptyList(),
                LocalDateTime.now()
        );
    }
}
