package pl.bodzioch.damian.service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.bodzioch.damian.service.ServiceType;

import java.util.Arrays;

public class ServiceTypeValidator implements ConstraintValidator<ServiceTypeV, String> {

    private final String[] allowedValues = Arrays.stream(ServiceType.values())
            .map(Enum::name)
            .toArray(String[]::new);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.asList(allowedValues).contains(value);
    }
}
