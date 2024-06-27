package pl.bodzioch.damian.configuration.filter;

import org.springframework.http.HttpMethod;

record Path(
        String path,
        HttpMethod method
) {
}
