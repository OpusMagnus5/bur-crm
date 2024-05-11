package pl.bodzioch.damian.utils.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProgramIdKindValidator.class)
public @interface ProgramIdKindV {

    String message() default "error.client.incorrectIdKind";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
