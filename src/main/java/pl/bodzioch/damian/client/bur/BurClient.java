package pl.bodzioch.damian.client.bur;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
class BurClient implements IBurClient {

    private static final String SERVICE_PROVIDERS_PATH = "dostawca-uslug";
    private static final String NIP_QUERY_PARAM = "nip";
    public static final String PAGE_QUERY_PARAM = "strona";
    public static final int FIRST_PAGE_PARAM_VALUE = 1;


    private final WebClient webClient;

    public BurClient(@Qualifier("burWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<Long> getServiceProviderBurId(Long nip) {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder.pathSegment(SERVICE_PROVIDERS_PATH)
                            .queryParam(NIP_QUERY_PARAM, nip)
                            .queryParam(PAGE_QUERY_PARAM, FIRST_PAGE_PARAM_VALUE)
                            .build())
                    .retrieve()
                    .bodyToMono(ServiceProviderResponse.class)
                    .map(response -> response.serviceProviders().getFirst().id().longValue());
    }
}
