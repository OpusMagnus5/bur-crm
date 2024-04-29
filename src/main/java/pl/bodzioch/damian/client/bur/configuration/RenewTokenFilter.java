package pl.bodzioch.damian.client.bur.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.*;
import pl.bodzioch.damian.client.bur.BurCredentialsProperties;
import pl.bodzioch.damian.client.bur.dto.AuthorizationRequest;
import pl.bodzioch.damian.client.bur.dto.AuthorizationResponse;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

@Component
class RenewTokenFilter implements ExchangeFilterFunction {

    private static final String AUTHORIZATION_PATH = "/autoryzacja/logowanie";

    private final BurCredentialsProperties burCredentials;
    private final WebClient webClient;

    public RenewTokenFilter(BurCredentialsProperties burCredentials, WebClient.Builder builder) {
        this.burCredentials = burCredentials;
        this.webClient = builder.baseUrl(BurClientConfig.BUR_URL + AUTHORIZATION_PATH).build();
    }

    @Override
    @NonNull
    public Mono<ClientResponse> filter(@NonNull ClientRequest request, ExchangeFunction next) {
        return next.exchange(request).flatMap(response -> {
            if (response.statusCode().value() == HttpStatus.UNAUTHORIZED.value()) {
                return response.releaseBody()
                        .then(authorize())
                        .flatMap(token -> next.exchange(request));
            } else {
                return Mono.just(response);
            }
        });
    }

    private Mono<String> authorize() {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.pathSegment(AUTHORIZATION_PATH).build())
                .bodyValue(new AuthorizationRequest(this.burCredentials))
                .retrieve()
                .bodyToMono(AuthorizationResponse.class)
                .doOnNext(response -> Token.setToken(response.token()))
                .map(AuthorizationResponse::token);
    }
}
