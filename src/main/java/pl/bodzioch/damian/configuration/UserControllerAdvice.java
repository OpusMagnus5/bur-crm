package pl.bodzioch.damian.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.bodzioch.damian.dto.AppErrorResponse;
import pl.bodzioch.damian.dto.ErrorDto;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.utils.MessageResolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@RequiredArgsConstructor
class UserControllerAdvice {

    private final MessageResolver messageResolver;

    @ExceptionHandler({AppException.class})
    ResponseEntity<AppErrorResponse> handleUserAppException(AppException e) {
        log.error(e.getMessage(), e);
        return UserGlobalControllerAdvice.getResponseEntity(e, messageResolver);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<AppErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        List<ErrorDto> errors = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> errors.add(
                new ErrorDto(
                        fieldError.getDefaultMessage(),
                        getMessage(fieldError)
                )));
        log.error(errors.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                .body(new AppErrorResponse(errors));
    }

    private String getMessage(FieldError fieldError) {
        return messageResolver.getMessage(
                Optional.ofNullable(fieldError.getDefaultMessage()).orElseThrow(() -> new AppException("No message found in exception")),
                Optional.ofNullable(fieldError.getRejectedValue()).map(Object::toString).map(List::of).orElse(Collections.emptyList()));
    }
}
