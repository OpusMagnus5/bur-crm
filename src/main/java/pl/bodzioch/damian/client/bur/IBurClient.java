package pl.bodzioch.damian.client.bur;

import reactor.core.publisher.Mono;

public interface IBurClient {

    Mono<Long> getServiceProviderBurId(Long nip);
}
