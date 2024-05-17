package pl.bodzioch.damian.client.bur;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import pl.bodzioch.damian.exception.HttpClientException;

import java.util.List;
import java.util.Optional;

@Component
class BurClient implements IBurClient {

    private static final String SERVICE_PROVIDERS_PATH = "dostawca-uslug";
    private static final String NIP_QUERY_PARAM = "nip";
    public static final String PAGE_QUERY_PARAM = "strona";
    public static final int FIRST_PAGE_PARAM_VALUE = 1;


    private final RestClient restClient;

    public BurClient(@Qualifier("burRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public BurServiceProviderDto getServiceProvider(Long nip) {
        ServiceProviderResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder.pathSegment(SERVICE_PROVIDERS_PATH)
                        .queryParam(NIP_QUERY_PARAM, nip.toString())
                        .queryParam(PAGE_QUERY_PARAM, FIRST_PAGE_PARAM_VALUE)
                        .build())
                .retrieve()
                .body(ServiceProviderResponse.class);
        return Optional.ofNullable(response).map(ServiceProviderResponse::serviceProviders)
                .map(List::getFirst)
                .map(provider -> new BurServiceProviderDto(provider.id().longValue(), provider.name()))
                .orElseThrow(() -> new HttpClientException("Empty service providers in response"));
    }
}
