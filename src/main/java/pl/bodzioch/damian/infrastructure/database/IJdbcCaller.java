package pl.bodzioch.damian.infrastructure.database;

import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import java.util.Map;

public interface IJdbcCaller {
    Map<String, Object> call(SimpleJdbcCall procedure, Map<String, Object> properties);
}
