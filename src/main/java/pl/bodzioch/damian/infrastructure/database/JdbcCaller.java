package pl.bodzioch.damian.infrastructure.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.util.StopWatch;

import java.util.Map;

@Slf4j
@Repository
class JdbcCaller implements IJdbcCaller {

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
        } catch (Exception e) {
            log.error("Error occurred while calling procedure.", e);
            throw e;
        } finally {
            stopWatch.stop();
            log.info("Procedure {} executed in {}ms with status {} and result: {}",
                    procedureName, stopWatch.getTotalTimeMillis(), callStatus, result);
        }
        return result;
    }
}
