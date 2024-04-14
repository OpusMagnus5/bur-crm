package pl.bodzioch.damian.dto;

import com.fasterxml.uuid.Generators;
import org.slf4j.MDC;
import pl.bodzioch.damian.exception.AppException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record AppErrorResponse(

        String errorId,
        LocalDateTime timestamp,
        String requestId,
        List<ErrorDto> errors

) implements Serializable {

    public AppErrorResponse(List<ErrorDto> errors) {
        this(
                Generators.timeBasedEpochGenerator().generate().toString(),
                LocalDateTime.now(),
                MDC.get(AppException.REQUEST_ID_MDC_PARAM),
                errors
        );
    }
}
