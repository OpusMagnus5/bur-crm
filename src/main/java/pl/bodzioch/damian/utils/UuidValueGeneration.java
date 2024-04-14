package pl.bodzioch.damian.utils;

import com.fasterxml.uuid.Generators;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.generator.EventTypeSets;

import java.util.EnumSet;

public class UuidValueGeneration implements BeforeExecutionGenerator {

    private final EnumSet<EventType> eventTypes;

    public UuidValueGeneration(GeneratedUuidValue annotation) {
        this.eventTypes = EventTypeSets.fromArray(annotation.types());
    }

    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o, Object o1, EventType eventType) {
        return Generators.timeBasedEpochGenerator().generate();
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return this.eventTypes;
    }
}
