package pl.bodzioch.damian.client.bur.configuration;

import io.netty.handler.logging.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.*;
import pl.bodzioch.damian.client.Dictionary;
import pl.bodzioch.damian.exception.AppException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;
import reactor.util.annotation.NonNull;

import static pl.bodzioch.damian.client.Dictionary.DictionaryKey.BUR;

@Slf4j
@Component
class RenewTokenFilter implements ExchangeFilterFunction {

    private static final String AUTHORIZATION_PATH = "/autoryzacja/logowanie";

    private final BurCredentialsProperties burCredentials;
    private final WebClient webClient;

    public RenewTokenFilter(BurCredentialsProperties burCredentials, WebClient.Builder builder) {
        this.burCredentials = burCredentials;

        HttpClient httpClient = HttpClient
                .create()
                .wiretap(HttpClient.class.getCanonicalName(), LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL);
        this.webClient = builder.baseUrl(BurClientConfig.BUR_URL + AUTHORIZATION_PATH)
                .filter(handleError())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Override
    @NonNull
    public Mono<ClientResponse> filter(@NonNull ClientRequest request, ExchangeFunction next) {
        return next.exchange(request).flatMap(response -> {
            if (response.statusCode().value() == HttpStatus.UNAUTHORIZED.value()) {
                return response.releaseBody()
                        .then(authorize())
                        .then(Mono.defer(() -> {
                            ClientRequest newRequest = ClientRequest.from(request).build();
                            return next.exchange(newRequest);
                        }));
            } else {
                return Mono.just(response);
            }
        });
    }

    private Mono<Void> authorize() {
        return webClient.post()
                .bodyValue(this.burCredentials.toAuthorizationRequest())
                .retrieve()
                .bodyToMono(AuthorizationResponse.class)
                .doOnNext(response -> Dictionary.put(BUR, response.token()))
                .then();
    }

    private ExchangeFilterFunction handleError() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            if (response.statusCode().isError()) {
                return Mono.error(new AppException("Exception occurred while calling BUR API"));
            }
            return Mono.just(response);
        });
    }
}
