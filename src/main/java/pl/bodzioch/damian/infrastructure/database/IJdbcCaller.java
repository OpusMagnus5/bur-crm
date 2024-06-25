package pl.bodzioch.damian.infrastructure.database;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import pl.bodzioch.damian.value_object.PageQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IJdbcCaller {
    Map<String, Object> call(SimpleJdbcCall procedure, Map<String, Object> properties);

    SimpleJdbcCall buildSimpleJdbcCall(String procedure);

    HashMap<String, Object> buildPageParams(PageQuery pageQuery);

    String getArrayCustomTypesParameter(CustomTypes customType, List<?> objects);

    @EventListener(ApplicationReadyEvent.class)
    void setCustomTypesDefinition();
}
