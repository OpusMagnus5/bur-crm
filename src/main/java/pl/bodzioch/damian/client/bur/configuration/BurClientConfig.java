package pl.bodzioch.damian.client.bur.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import pl.bodzioch.damian.client.bur.BurCredentialsProperties;

@EnableConfigurationProperties(BurCredentialsProperties.class)
@Configuration
@RequiredArgsConstructor
class BurClientConfig {

    protected static final String BUR_URL = "https://uslugirozwojowe.parp.gov.pl/api";

    private final RenewTokenFilter renewTokenFilter;

    @Bean("burWebClient")
    WebClient burWebClient(WebClient.Builder builder) {
        return builder.baseUrl(BUR_URL)
                .filter(renewTokenFilter)
                .filter(new TokenHeaderFilter())
                .build();
    }
}

