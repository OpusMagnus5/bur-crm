package pl.bodzioch.damian.client.bur.configuration;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

class TokenHeaderFilter implements ExchangeFilterFunction {

    @Override
    @NonNull
    public Mono<ClientResponse> filter(@NonNull ClientRequest request, ExchangeFunction next) {
        ClientRequest newRequest = ClientRequest.from(request).header("Authorization", "Bearer " + Token.getToken()).build();
        return next.exchange(newRequest);
    }
}
