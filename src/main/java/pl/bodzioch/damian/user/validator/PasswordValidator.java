package pl.bodzioch.damian.user.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    static final String ALLOWED_CAPITAL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String ALLOWED_LOWER_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    static final String ALLOWED_DIGITS = "0123456789";
    static final String ALLOWED_SPECIAL_CHARS = "!@#$%^&*()";
    public static final String ALLOWED_PASSWORD_CHARS = ALLOWED_CAPITAL_LETTERS + ALLOWED_LOWER_LETTERS + ALLOWED_DIGITS + ALLOWED_SPECIAL_CHARS;
    public static final int MIN_PASSWORD_LENGTH = 12;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isNotBlank(value) &&
                StringUtils.containsAny(value, ALLOWED_PASSWORD_CHARS) &&
                StringUtils.containsAny(value, ALLOWED_LOWER_LETTERS) &&
                StringUtils.containsAny(value, ALLOWED_DIGITS) &&
                StringUtils.containsAny(value, ALLOWED_SPECIAL_CHARS) &&
                value.length() >= MIN_PASSWORD_LENGTH;
    }
}
