package pl.bodzioch.damian.configuration;

import com.fasterxml.uuid.Generators;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.utils.PermissionService;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@WebFilter("/api/*")
@RequiredArgsConstructor
class RequestIdFilter implements Filter {

    private final PermissionService permissionService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        MDC.put(AppException.REQUEST_ID_MDC_PARAM, Generators.timeBasedEpochGenerator().generate().toString());
        Optional<UUID> sessionId = permissionService.getSessionId();
        if (sessionId.isPresent()) {
            MDC.put("sessionId", sessionId.toString());
        }
        chain.doFilter(request, response);
    }
}
