package pl.bodzioch.damian.configuration.filter;

import com.fasterxml.uuid.Generators;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.configuration.security.SecurityConstants;
import pl.bodzioch.damian.exception.AppException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
@Order(1)
@WebFilter("/api/*")
class MdcFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        String sessionId = this.getSessionId(request);
        MDC.put(AppException.REQUEST_ID_MDC_PARAM, Generators.timeBasedEpochGenerator().generate().toString());
        MDC.put("ip", ip);
        MDC.put("sessionId", sessionId);

        chain.doFilter(request, response);
    }

    private String getSessionId(ServletRequest request) { //TODO poprawić nie z cookie tylko z jwt, dodac id usera, uzupelnic czerwone logowanie
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        return Arrays.stream(Optional.ofNullable(httpServletRequest.getCookies()).orElse(new Cookie[]{}))
                .filter(cookie -> cookie.getName().equals(SecurityConstants.SESSION_ID))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("");
    }
}
