package pl.bodzioch.damian.configuration.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import pl.bodzioch.damian.error.command_dto.SaveErrorCommand;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.command.CommandExecutor;
import pl.bodzioch.damian.utils.CipherComponent;
import pl.bodzioch.damian.utils.MessageResolver;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Order
@RestControllerAdvice
@RequiredArgsConstructor
class UserGlobalControllerAdvice {

    private final MessageResolver messageResolver;
    private final CipherComponent cipher;
    private final CommandExecutor commandExecutor;

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<AppErrorResponse> handleUserAppException(Exception e) {
        log.error(e.getMessage(), e);
        AppException generalError = AppException.getGeneralError();
        log.error(generalError.toString());
        saveError(e, generalError);
        return getResponseEntity(generalError, messageResolver);
    }

    static ResponseEntity<AppErrorResponse> getResponseEntity(AppException exception, MessageResolver messageResolver) {
        List<ErrorDto> errorsDto = new ArrayList<>();
        exception.getErrors().forEach(e -> {
            String errorCode = e.errorCode();
            List<String> messageParams = e.parameters();
            String errorDetail = messageResolver.getMessage(errorCode, messageParams);
            errorsDto.add(new ErrorDto(errorCode, errorDetail));
        });

        return ResponseEntity.status(exception.getHttpStatus())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                .body(new AppErrorResponse(errorsDto));
    }

    private void saveError(Exception e, AppException appEx) {
        pl.bodzioch.damian.error.ErrorDto errorDto =
                new pl.bodzioch.damian.error.ErrorDto(e, appEx, cipher.getPrincipalIdIfExists().orElse(null));
        SaveErrorCommand command = new SaveErrorCommand(errorDto);
        commandExecutor.executeAsync(command);
    }
}
