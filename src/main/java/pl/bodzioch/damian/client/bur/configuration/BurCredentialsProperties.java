package pl.bodzioch.damian.client.bur.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bur")
record BurCredentialsProperties(String username, String key) {

    AuthorizationRequest toAuthorizationRequest() {
        return new AuthorizationRequest(this.username, this.key);
    }
}
