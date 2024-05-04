package pl.bodzioch.damian.operator;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import javax.sql.DataSource;

@Repository
class OperatorWriteRepository implements IOperatorWriteRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall createNewProc;

    public OperatorWriteRepository(IJdbcCaller jdbcCaller, DataSource dataSource) {
        this.jdbcCaller = jdbcCaller;
        this.createNewProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "operator_create_new");
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createNew(Operator operator) {
        this.jdbcCaller.call(this.createNewProc, operator.getPropertySource());
    }
}
