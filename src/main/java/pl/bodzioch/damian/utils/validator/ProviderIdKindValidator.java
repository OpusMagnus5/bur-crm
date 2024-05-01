package pl.bodzioch.damian.utils.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.bodzioch.damian.service_provider.ServiceProviderIdKind;

import java.util.Arrays;

public class ProviderIdKindValidator implements ConstraintValidator<ProviderIdKindV, String> {

    private final String[] allowedValues = Arrays.stream(ServiceProviderIdKind.values())
            .map(Enum::name)
            .toArray(String[]::new);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.asList(allowedValues).contains(value);
    }
}
