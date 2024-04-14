package pl.bodzioch.damian.configuration;

import com.fasterxml.uuid.Generators;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;

import java.io.IOException;

@Component
@WebFilter("/api/*")
class RequestIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        MDC.put(AppException.REQUEST_ID_MDC_PARAM, Generators.timeBasedEpochGenerator().generate().toString());
        chain.doFilter(request, response);
    }
}
