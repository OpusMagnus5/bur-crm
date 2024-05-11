package pl.bodzioch.damian.utils.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.bodzioch.damian.program.ProgramIdKind;

import java.util.Arrays;

public class ProgramIdKindValidator implements ConstraintValidator<ProgramIdKindV, String> {

    private final String[] allowedValues = Arrays.stream(ProgramIdKind.values())
            .map(Enum::name)
            .toArray(String[]::new);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.asList(allowedValues).contains(value);
    }
}
