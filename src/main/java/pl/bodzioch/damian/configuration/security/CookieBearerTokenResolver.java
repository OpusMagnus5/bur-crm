package pl.bodzioch.damian.configuration.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

import java.util.Arrays;

import static pl.bodzioch.damian.user.GenerateJwtTokenCommandHandler.BEARER_COOKIE;

class CookieBearerTokenResolver implements BearerTokenResolver {

    @Override
    public String resolve(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> BEARER_COOKIE.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
