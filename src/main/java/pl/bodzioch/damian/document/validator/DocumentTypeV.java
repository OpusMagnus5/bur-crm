package pl.bodzioch.damian.document.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pl.bodzioch.damian.service.validator.ServiceStatusValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ServiceStatusValidator.class)
public @interface DocumentTypeV {
    String message() default "Invalid value";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
