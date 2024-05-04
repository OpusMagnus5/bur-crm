package pl.bodzioch.damian.infrastructure.database;

import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import javax.sql.DataSource;
import java.util.Map;

public interface IJdbcCaller {
    Map<String, Object> call(SimpleJdbcCall procedure, Map<String, Object> properties);

    SimpleJdbcCall buildSimpleJdbcCall(DataSource dataSource, String procedure);
}
