package pl.bodzioch.damian.operator;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.DbCaster;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
class OperatorWriteRepository implements IOperatorWriteRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall createNewProc;
    private final SimpleJdbcCall deleteProc;
    private final SimpleJdbcCall updateProc;

    public OperatorWriteRepository(IJdbcCaller jdbcCaller, DataSource dataSource) {
        this.jdbcCaller = jdbcCaller;
        this.createNewProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "operator_create_new");
        this.deleteProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "operator_delete");
        this.updateProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "operator_update");
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createNew(Operator operator) {
        Map<String, Object> properties = DbCaster.toProperties(operator);
        this.jdbcCaller.call(this.createNewProc, properties);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(Long id) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_opr_id", id);
        this.jdbcCaller.call(this.deleteProc, properties);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void update(Operator operator) {
        Map<String, Object> properties = DbCaster.toProperties(operator);
        this.jdbcCaller.call(this.updateProc, properties);
    }
}
