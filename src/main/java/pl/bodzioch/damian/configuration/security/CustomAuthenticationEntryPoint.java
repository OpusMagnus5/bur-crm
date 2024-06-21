package pl.bodzioch.damian.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.dto.AppErrorResponse;
import pl.bodzioch.damian.dto.ErrorDto;
import pl.bodzioch.damian.utils.MessageResolver;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String ERROR_CLIENT_AUTHENTICATION_MESSAGE_CODE = "error.client.authenticationError";

    private final MessageResolver messageResolver;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String message = messageResolver.getMessage(ERROR_CLIENT_AUTHENTICATION_MESSAGE_CODE, request);
        ErrorDto error = new ErrorDto(ERROR_CLIENT_AUTHENTICATION_MESSAGE_CODE, message);
        AppErrorResponse errorResponse = new AppErrorResponse(List.of(error));
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        new ObjectMapper().registerModule(new JavaTimeModule())
                .writeValue(response.getOutputStream(), errorResponse);
    }
}
