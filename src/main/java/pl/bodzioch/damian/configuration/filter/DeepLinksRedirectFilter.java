package pl.bodzioch.damian.configuration.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Order(1)
@Component
@WebFilter("/*")
class DeepLinksRedirectFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String path = httpServletRequest.getServletPath();
        if (!path.startsWith("/api") && !path.equals("/") && !path.contains(".")) {
            request.getRequestDispatcher("/index.html").forward(request, response);
            return;
        }

        chain.doFilter(request, response);
    }
}
