package pl.bodzioch.damian.infrastructure.database;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.util.StopWatch;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.value_object.ErrorData;
import pl.bodzioch.damian.value_object.PageQuery;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
class JdbcCaller implements IJdbcCaller {

    private static final String OPTIMISTIC_LOCKING_SQL_STATE = "55000";
    private static final String FOREIGN_KEY_VIOLATION_SQL_STATE = "23503";

    @Override
    public Map<String, Object> call(SimpleJdbcCall procedure, Map<String, Object> properties) {
        Map<String, Object> result = null;
        JdbcCallStatus callStatus = JdbcCallStatus.FAIL;
        String procedureName = procedure.getProcedureName();
        log.info("Call procedure {} with properties: {}", procedureName, properties);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            result = procedure.execute(properties);
            callStatus = JdbcCallStatus.SUCCESS;
        } catch (UncategorizedSQLException e) {
            handleUncategorizedSQLException(e);
        } catch (DataIntegrityViolationException e) {
            handleDataIntegrityViolationException(e);
        }
        catch (Exception e) {
            log.error("Error occurred while calling procedure.", e);
            throw e;
        } finally {
            stopWatch.stop();
            log.info("Procedure {} executed in {}ms with status {} and result: {}",
                    procedureName, stopWatch.getTotalTimeMillis(), callStatus, result);
        }
        return result;
    }

    @Override
    public SimpleJdbcCall buildSimpleJdbcCall(DataSource dataSource, String procedure) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        return new SimpleJdbcCall(jdbcTemplate).withProcedureName(procedure);
    }

    @Override
    public HashMap<String, Object> buildPageParams(PageQuery pageQuery) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_offset", pageQuery.getFirstResult());
        properties.put("_max", pageQuery.getMaxResult());
        properties.putAll(pageQuery.filtersToDbProperties());
        return properties;
    }

    private void handleUncategorizedSQLException(UncategorizedSQLException e) {
        if (OPTIMISTIC_LOCKING_SQL_STATE.equals(e.getSQLException().getSQLState())) {
            log.warn("Optimistic locking error", e);
            throw buildOptimisticLockingException();
        } else {
            log.error("Error occurred while calling procedure.", e);
            throw e;
        }
    }

    private void handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        PSQLException cause = (PSQLException) e.getCause();
        String sqlState = cause.getSQLState();
        if (FOREIGN_KEY_VIOLATION_SQL_STATE.equals(sqlState)) {
            throw buildForeignKeyViolationException();
        } else {
            log.error("Error occurred while calling procedure.", e);
            throw e;
        }
    }

    private AppException buildOptimisticLockingException() {
        return new AppException(
                "Optimistic locking error",
                HttpStatus.CONFLICT,
                List.of(new ErrorData("error.client.optimisticLocking", Collections.emptyList()))
        );
    }

    private AppException buildForeignKeyViolationException() {
        return new AppException(
                "Foreign key violation error",
                HttpStatus.UNPROCESSABLE_ENTITY,
                List.of(new ErrorData("error.client.foreignKeyViolation", Collections.emptyList()))
        );
    }
}
