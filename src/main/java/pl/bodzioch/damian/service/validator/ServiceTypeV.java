package pl.bodzioch.damian.service.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ServiceTypeValidator.class)
public @interface ServiceTypeV {
    String message() default "Invalid value";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
