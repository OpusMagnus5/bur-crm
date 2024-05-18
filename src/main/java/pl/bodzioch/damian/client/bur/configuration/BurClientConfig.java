package pl.bodzioch.damian.client.bur.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;
import pl.bodzioch.damian.client.Dictionary;
import pl.bodzioch.damian.exception.HttpClientException;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

import static pl.bodzioch.damian.client.Dictionary.DictionaryKey.BUR;

@Slf4j
@EnableConfigurationProperties(BurCredentialsProperties.class)
@Configuration
@RequiredArgsConstructor
class BurClientConfig {
    private final List<ClientHttpRequestInterceptor> interceptors = List.of(
            logRequestInterceptor(),
            addTokenHeaderInterceptor(),
            renewTokenInterceptor(),
            logResponseInterceptor()
    );

    protected static final String BUR_URL = "https://uslugirozwojowe.parp.gov.pl/api";
    private static final String AUTHORIZATION_PATH = "/autoryzacja/logowanie";

    private final BurCredentialsProperties burCredentials;

    @Bean("burRestClient")
    RestClient burWebClient() {
        return RestClient.builder()
                .baseUrl(BUR_URL)
                .requestInterceptors(interceptors -> interceptors.addAll(this.interceptors))
                .build();
    }

    private ClientHttpRequestInterceptor renewTokenInterceptor() {
        return (request, body, execution) -> {
            ClientHttpResponse response = execution.execute(request, body);
            if (response.getStatusCode().value() == HttpStatus.UNAUTHORIZED.value()) {
                Dictionary.put(BUR, getRefreshedToken());
                return addTokenHeaderInterceptor().intercept(request, body, execution);
            }
            return response;
        };
    }

    private ClientHttpRequestInterceptor addTokenHeaderInterceptor() {
        return (request, body, execution) -> {
            request.getHeaders().set("Authorization", "Bearer " + Dictionary.get(BUR));
            return execution.execute(request, body);
        };
    }

    private ClientHttpRequestInterceptor logRequestInterceptor() {
        return (request, body, execution) -> {
            log.info("{} {}", request.getMethod(), request.getURI());
            log.info("Headers: {}", request.getHeaders());
            log.info("Body: {}", new String(body, Charset.defaultCharset()));
            return execution.execute(request, body);
        };
    }

    private ClientHttpRequestInterceptor logResponseInterceptor() {
        return (request, body, execution) -> {
            ClientHttpResponse response = execution.execute(request, body);
            byte[] bodyBytes = IOUtils.toByteArray(response.getBody());
            BufferingClientHttpResponseWrapper responseWrapper = new BufferingClientHttpResponseWrapper(response, bodyBytes);
            log.info("{} {}", request.getMethod(), request.getURI());
            log.info("Status: {}", response.getStatusCode().value());
            log.info("Headers: {}", response.getHeaders());
            log.info("Body: {}", new String(bodyBytes, Charset.defaultCharset()));
            return responseWrapper;
        };
    }

    private String getRefreshedToken() {
        AuthorizationResponse response = RestClient.builder()
                .baseUrl(BUR_URL + AUTHORIZATION_PATH)
                .requestInterceptors(interceptors -> interceptors.addAll(List.of(logRequestInterceptor(), logResponseInterceptor())))
                .build()
                .post()
                .body(this.burCredentials.toAuthorizationRequest())
                .retrieve()
                .body(AuthorizationResponse.class);
        return Optional.ofNullable(response)
                .map(AuthorizationResponse::token)
                .orElseThrow(() -> new HttpClientException("Empty auth token in bur response"));
    }
}

