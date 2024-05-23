package pl.bodzioch.damian.user.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class UserRoleValidator implements ConstraintValidator<UserRoleV, String> {

    private final String[] allowedValues = Arrays.stream(pl.bodzioch.damian.user.UserRole.values())
            .map(Enum::name)
            .toArray(String[]::new);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.asList(allowedValues).contains(value);
    }
}
