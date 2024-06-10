package pl.bodzioch.damian.document.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.bodzioch.damian.document.DocumentType;

import java.util.Arrays;

public class DocumentTypeValidator implements ConstraintValidator<DocumentTypeV, String> {

    private final String[] allowedValues = Arrays.stream(DocumentType.values())
            .map(Enum::name)
            .toArray(String[]::new);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.asList(allowedValues).contains(value);
    }
}
