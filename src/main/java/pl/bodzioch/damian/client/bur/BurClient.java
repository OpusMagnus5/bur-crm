package pl.bodzioch.damian.client.bur;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
class BurClient implements IBurClient {

    private final WebClient webClient;

    public BurClient(@Qualifier("burWebClient") WebClient webClient, BurCredentialsProperties burCredentials) {
        this.webClient = webClient;
    }
}
