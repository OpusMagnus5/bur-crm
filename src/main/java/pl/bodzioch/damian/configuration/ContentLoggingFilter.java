package pl.bodzioch.damian.configuration;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@Slf4j
@Component
@WebFilter("/api/*")
class ContentLoggingFilter implements Filter {

    private static final List<Path> excludedBodyRequestLogg = List.of(
            new Path("/api/user/login", HttpMethod.POST)
    );

    private static final List<Path> excludedBodyResponseLogg = List.of(
            new Path("/api/user", HttpMethod.POST),
            new Path("/api/user/login", HttpMethod.POST),
            new Path("/api/user/reset-password", HttpMethod.PATCH)
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpServletResponse);

        String requestURI = httpServletRequest.getRequestURI();
        log.info("REQUEST {} {}", requestWrapper.getMethod(), requestURI);

        chain.doFilter(requestWrapper, responseWrapper);

        boolean isNotExcludedRequest = excludedBodyRequestLogg.stream().noneMatch(path ->
                requestURI.contains(path.path()) && requestWrapper.getMethod().equals(path.method().name()));
        boolean isNotExcludedResponse = excludedBodyResponseLogg.stream().noneMatch(path ->
                requestURI.contains(path.path()) && httpServletRequest.getMethod().equals(path.method().name()));

        if (isNotExcludedRequest) {
            log.info("REQUEST Body: {}", requestWrapper.getContentAsString());
        }
        log.info("RESPONSE {} {}", httpServletRequest.getMethod(), httpServletRequest.getRequestURI());
        if (isNotExcludedResponse) {
            log.info("RESPONSE Status: {}", responseWrapper.getStatus());
            log.info("RESPONSE Body: {}", new String(responseWrapper.getContentInputStream().readAllBytes(), Charset.defaultCharset()));
        }
        responseWrapper.copyBodyToResponse();
    }
}
