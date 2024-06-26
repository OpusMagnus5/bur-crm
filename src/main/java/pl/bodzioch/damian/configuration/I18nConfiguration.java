package pl.bodzioch.damian.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.time.temporal.ChronoUnit;
import java.util.Locale;

import static pl.bodzioch.damian.utils.MessageResolver.CLIENT_LANGUAGE_COOKIE;
import static pl.bodzioch.damian.utils.MessageResolver.DEFAULT_LANGUAGE;

@Configuration
class I18nConfiguration {


    @Bean
    LocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver(CLIENT_LANGUAGE_COOKIE);
        localeResolver.setCookieMaxAge(ChronoUnit.FOREVER.getDuration());
        localeResolver.setDefaultLocale(Locale.of(DEFAULT_LANGUAGE));
        return localeResolver;
    }

    @Bean
    MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }
}
