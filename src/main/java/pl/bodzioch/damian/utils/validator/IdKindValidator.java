package pl.bodzioch.damian.utils.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.bodzioch.damian.user.IdKind;

import java.util.Arrays;

public class IdKindValidator implements ConstraintValidator<IdKindV, String> {

    private final String[] allowedValues = Arrays.stream(IdKind.values())
            .map(Enum::name)
            .toArray(String[]::new);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.asList(allowedValues).contains(value);
    }
}
