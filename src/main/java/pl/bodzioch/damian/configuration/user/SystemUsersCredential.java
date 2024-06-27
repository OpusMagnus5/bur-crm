package pl.bodzioch.damian.configuration.user;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "system")
record SystemUsersCredentials(
        Map<Long, String> users
) {
}
