package pl.bodzioch.damian.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageResolver {
    public static final String CLIENT_LANGUAGE_COOKIE = "client-language";
    public static final String DEFAULT_LANGUAGE = "pl";

    private final MessageSource messageSource;

    public String getMessage(String code, List<String> params) {
       return messageSource.getMessage(code, params.toArray(), LocaleContextHolder.getLocale());
    }

    public String getMessage(String code) {
        return messageSource.getMessage(code, new Object[]{}, LocaleContextHolder.getLocale());
    }

    public String getMessage(String code, HttpServletRequest request) {
        return messageSource.getMessage(code, new Object[]{}, getLocaleFromRequest(request));
    }

    private Locale getLocaleFromRequest(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(item -> CLIENT_LANGUAGE_COOKIE.equals(item.getName()))
                .map(Cookie::getValue)
                .map(Locale::of)
                .findFirst()
                .orElse(Locale.of(DEFAULT_LANGUAGE));
    }
}
