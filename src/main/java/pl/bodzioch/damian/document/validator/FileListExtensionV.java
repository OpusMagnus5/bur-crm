package pl.bodzioch.damian.document.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileListExtensionValidator.class)
public @interface FileListExtensionV {
    String message() default "Invalid value";
    String[] extensions();
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
