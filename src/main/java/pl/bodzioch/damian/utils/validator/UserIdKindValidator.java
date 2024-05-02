package pl.bodzioch.damian.utils.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.bodzioch.damian.user.UserIdKind;

import java.util.Arrays;

public class UserIdKindValidator implements ConstraintValidator<UserIdKindV, String> {

    private final String[] allowedValues = Arrays.stream(UserIdKind.values())
            .map(Enum::name)
            .toArray(String[]::new);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.asList(allowedValues).contains(value);
    }
}