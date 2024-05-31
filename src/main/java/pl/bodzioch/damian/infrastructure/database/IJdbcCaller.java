package pl.bodzioch.damian.infrastructure.database;

import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import pl.bodzioch.damian.value_object.PageQuery;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public interface IJdbcCaller {
    Map<String, Object> call(SimpleJdbcCall procedure, Map<String, Object> properties);

    SimpleJdbcCall buildSimpleJdbcCall(DataSource dataSource, String procedure);

    HashMap<String, Object> buildPageParams(PageQuery pageQuery);
}
