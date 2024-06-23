package pl.bodzioch.damian.configuration;

import org.springframework.http.HttpMethod;

record Path(
        String path,
        HttpMethod method
) {
}
