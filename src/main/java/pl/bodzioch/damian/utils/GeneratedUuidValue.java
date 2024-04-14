package pl.bodzioch.damian.utils;

import org.hibernate.annotations.IdGeneratorType;
import org.hibernate.annotations.ValueGenerationType;
import org.hibernate.generator.EventType;

import java.lang.annotation.*;

@IdGeneratorType(value = UuidValueGeneration.class)
@ValueGenerationType(generatedBy = UuidValueGeneration.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Inherited
public @interface GeneratedUuidValue {

    EventType[] types();
}
