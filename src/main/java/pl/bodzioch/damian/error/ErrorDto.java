package pl.bodzioch.damian.error;

import pl.bodzioch.damian.dto.SaveWebErrorRequest;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.user.InnerUserDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public record ErrorDto(
       Long id,
       UUID uuid,
       Long userId,
       LocalDateTime date,
       String clazz,
       String message,
       String stacktrace,
       Boolean isWeb,
       InnerUserDto user
) {

    public ErrorDto(Exception ex, AppException appEx, Long userId) {
        this(
                null, appEx.getErrorId(), userId, appEx.getOccurredAt(), ex.getClass().getCanonicalName(), ex.getMessage(),
                Arrays.stream(ex.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.joining("\n")),
                false, null
        );
    }

    public ErrorDto(Exception ex) {
        this(
                null, null, null, LocalDateTime.now(), ex.getClass().getCanonicalName(), ex.getMessage(),
                Arrays.stream(ex.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.joining("\n")),
                false, null
        );
    }

    public ErrorDto(SaveWebErrorRequest request, Long userId) {
        this(
                null, null, userId, LocalDateTime.now(), request.clazz(), request.message(), request.stacktrace(), true, null
        );
    }
}
