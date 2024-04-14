package pl.bodzioch.damian.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageResolver {

    private final MessageSource messageSource;

    public String getMessage(String code, List<String> params) {
       return messageSource.getMessage(code, params.toArray(), LocaleContextHolder.getLocale());
    }

    public String getMessage(String code) {
        return messageSource.getMessage(code, new Object[]{}, LocaleContextHolder.getLocale());
    }
}
