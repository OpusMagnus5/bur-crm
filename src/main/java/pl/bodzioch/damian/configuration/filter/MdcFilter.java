package pl.bodzioch.damian.configuration.filter;

import com.fasterxml.uuid.Generators;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.utils.CipherComponent;

import java.io.IOException;

@Slf4j
@Component
@Order(1)
@WebFilter("/api/*")
@RequiredArgsConstructor
class MdcFilter implements Filter {

    private final CipherComponent cipher;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        MDC.put(AppException.REQUEST_ID_MDC_PARAM, Generators.timeBasedEpochGenerator().generate().toString());
        MDC.put("ip", request.getRemoteAddr());
        MDC.put("sessionId", this.cipher.getSessionId().orElse(""));
        MDC.put("userId", cipher.getPrincipalIdIfExists().map(String::valueOf).orElse(""));

        chain.doFilter(request, response);
    }
}
