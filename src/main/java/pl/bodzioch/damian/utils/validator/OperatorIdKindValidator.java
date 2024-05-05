package pl.bodzioch.damian.utils.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.bodzioch.damian.operator.OperatorIdKind;

import java.util.Arrays;

public class OperatorIdKindValidator implements ConstraintValidator<OperatorIdKindV, String> {

    private final String[] allowedValues = Arrays.stream(OperatorIdKind.values())
            .map(Enum::name)
            .toArray(String[]::new);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.asList(allowedValues).contains(value);
    }
}
