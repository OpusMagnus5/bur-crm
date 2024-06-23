package pl.bodzioch.damian.configuration;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
        log.error(e.toString(), e);
        return UserGlobalControllerAdvice.getResponseEntity(e, messageResolver);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    ResponseEntity<AppErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        List<ErrorDto> errors = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> errors.add(
                new ErrorDto(
                        fieldError.getDefaultMessage(),
                        getMessage(fieldError)
                )));
        AppErrorResponse body = new AppErrorResponse(errors);
        log.error(body.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                .body(body);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    ResponseEntity<AppErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        List<ErrorDto> errors = new ArrayList<>();
        e.getConstraintViolations().forEach(violation -> errors.add(
                new ErrorDto(
                violation.getMessage(),
                getMessage(violation)
        )));
        AppErrorResponse body = new AppErrorResponse(errors);
        log.error(body.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                .body(body);
    }

    @ExceptionHandler({ AccessDeniedException.class })
    ResponseEntity<AppErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("Access Denied!", e);
        String accessDeniedCode = "client.error.accessDenied";
        String message = messageResolver.getMessage(accessDeniedCode);
        ErrorDto errorDto = new ErrorDto(accessDeniedCode, message);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new AppErrorResponse(List.of(errorDto)));
    }

    private String getMessage(FieldError fieldError) {
        return messageResolver.getMessage(
                Optional.ofNullable(fieldError.getDefaultMessage()).orElseThrow(() -> new AppException("No message found in exception")),
                Optional.ofNullable(fieldError.getRejectedValue()).map(Object::toString).map(List::of).orElse(Collections.emptyList()));
    }

    @SuppressWarnings("rawtypes")
    private String getMessage(ConstraintViolation fieldError) {
        return messageResolver.getMessage(
                Optional.ofNullable(fieldError.getMessage()).orElseThrow(() -> new AppException("No message found in exception")),
                Optional.ofNullable(fieldError.getInvalidValue()).map(Object::toString).map(List::of).orElse(Collections.emptyList()));
    }
}
