package pl.bodzioch.damian.client.bur.configuration;

import io.netty.handler.logging.LogLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import pl.bodzioch.damian.client.Dictionary;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import static pl.bodzioch.damian.client.Dictionary.DictionaryKey.BUR;

@Slf4j
@EnableConfigurationProperties(BurCredentialsProperties.class)
@Configuration
@RequiredArgsConstructor
class BurClientConfig {

    protected static final String BUR_URL = "https://uslugirozwojowe.parp.gov.pl/api";

    private final RenewTokenFilter renewTokenFilter;

    @Bean("burWebClient")
    WebClient burWebClient(WebClient.Builder builder) {
        HttpClient httpClient = HttpClient
                .create()
                .wiretap(HttpClient.class.getCanonicalName(), LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL);
        return builder.baseUrl(BUR_URL)
                .filter(renewTokenFilter)
                .filter(tokenHeaderFilter())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    private ExchangeFilterFunction tokenHeaderFilter() {
        return (request, next) -> {
            ClientRequest newRequest = ClientRequest
                    .from(request)
                    .header("Authorization", "Bearer " + Dictionary.get(BUR))
                    .build();
            return next.exchange(newRequest);
        };
    }
}

