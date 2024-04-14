package pl.bodzioch.damian.valueobject;

import java.util.Collections;
import java.util.List;

public record ErrorData(
        String errorCode,
        List<String> parameters

) {
    public static final String GENERAL_ERROR_CODE = "error.server.general";

    public static ErrorData getGeneralErrorData() {
        return new ErrorData(
                GENERAL_ERROR_CODE,
                Collections.emptyList()
        );
    }
}
