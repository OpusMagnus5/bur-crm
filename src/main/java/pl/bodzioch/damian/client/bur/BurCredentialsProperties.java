package pl.bodzioch.damian.client.bur;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bur")
public record BurCredentialsProperties(String username, String key) {
}
