package pl.bodzioch.damian.configuration.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Order(3)
@Component
@WebFilter("/api/*")
class ContentLoggingFilter implements Filter {

    private static final List<Path> excludedBodyRequestLogg = List.of(
            new Path("/api/user/login", HttpMethod.POST),
            new Path("/api/user/change-password", HttpMethod.PATCH)
    );

    private static final List<Path> excludedBodyResponseLogg = List.of(
            new Path("/api/user", HttpMethod.POST),
            new Path("/api/user/login", HttpMethod.POST),
            new Path("/api/user/reset-password", HttpMethod.PATCH)
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String path = httpServletRequest.getServletPath();
        if (!path.startsWith("/api")) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        CustomCachingRequestWrapper requestWrapper = new CustomCachingRequestWrapper(httpServletRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpServletResponse);
        
        logRequest(requestWrapper);
        chain.doFilter(requestWrapper, responseWrapper);
        logResponse(requestWrapper, responseWrapper);
    }

    private void logRequest(CustomCachingRequestWrapper requestWrapper) {
        String requestURI = requestWrapper.getRequestURI();
        boolean isNotExcludedRequest = excludedBodyRequestLogg.stream().noneMatch(path ->
                requestURI.contains(path.path()) && requestWrapper.getMethod().equals(path.method().name()));
        
        log.info("REQUEST {} {}", requestWrapper.getMethod(), requestURI);
        
        String requestParameters = getRequestParameters(requestWrapper);
        if (StringUtils.isNotBlank(requestParameters)) {
            log.info("REQUEST PARAMS {}", requestParameters);
        }
        
        String body = requestWrapper.getContentAsString();
        if (isNotExcludedRequest && StringUtils.isNotBlank(body)) {
            log.info("REQUEST Body: {}", body);
        }
    }
    
    private void logResponse(CustomCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper) throws IOException {
        String requestURI = requestWrapper.getRequestURI();
        boolean isNotExcludedResponse = excludedBodyResponseLogg.stream().noneMatch(path ->
                requestURI.contains(path.path()) && requestWrapper.getMethod().equals(path.method().name()));
        
        log.info("RESPONSE {} {}", requestWrapper.getMethod(), requestWrapper.getRequestURI());
        log.info("RESPONSE Status: {}", responseWrapper.getStatus());

        String body = new String(responseWrapper.getContentAsByteArray(), Charset.defaultCharset());
        if (isNotExcludedResponse && StringUtils.isNotBlank(body)) {
            log.info("RESPONSE Body: {}", body);
        }
        responseWrapper.copyBodyToResponse();
    }

    private String getRequestParameters(CustomCachingRequestWrapper requestWrapper) {
        return requestWrapper.getParameterMap().entrySet().stream()
                .map(entry -> entry.getKey() + ": " + Arrays.toString(entry.getValue()))
                .collect(Collectors.joining(", "));
    }
}
