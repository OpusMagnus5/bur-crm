package pl.bodzioch.damian.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.bodzioch.damian.dto.AppErrorResponse;
import pl.bodzioch.damian.dto.ErrorDto;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.utils.MessageResolver;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Order(2)
@RestControllerAdvice
@RequiredArgsConstructor
class UserGlobalControllerAdvice {

    private final MessageResolver messageResolver;

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<AppErrorResponse> handleUserAppException(Exception e) {
        log.error(e.getMessage(), e);
        AppException generalError = AppException.getGeneralError();
        log.error(e.getMessage(), e);
        return getResponseEntity(generalError, messageResolver);
    }

    static ResponseEntity<AppErrorResponse> getResponseEntity(AppException exception, MessageResolver messageResolver) {
        List<ErrorDto> errorsDto = new ArrayList<>();
        exception.getErrors().forEach(e -> {
            String errorCode = e.errorCode();
            List<String> messageParams = e.parameters();
            String errorDetail = messageResolver.getMessage(errorCode, messageParams);
            errorsDto.add(new ErrorDto(errorDetail, MDC.get(AppException.REQUEST_ID_MDC_PARAM)));
        });

        return ResponseEntity.status(exception.getHttpStatus())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                .body(new AppErrorResponse(errorsDto));
    }
}
