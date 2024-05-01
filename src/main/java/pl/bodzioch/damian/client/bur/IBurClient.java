package pl.bodzioch.damian.client.bur;

import reactor.core.publisher.Mono;

public interface IBurClient {

    Mono<BurServiceProviderDto> getServiceProvider(Long nip);
}
