package pl.bodzioch.damian.intermediary;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.DbCaster;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
class IntermediaryWriteRepository implements IIntermediaryWriteRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall createNewProc;
    private final SimpleJdbcCall deleteProc;
    private final SimpleJdbcCall updateProc;

    IntermediaryWriteRepository(IJdbcCaller jdbcCaller, DataSource dataSource) {
        this.jdbcCaller = jdbcCaller;
        this.createNewProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "intermediary_create_new");
        this.deleteProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "intermediary_delete");
        this.updateProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "intermediary_update");

    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createNew(Intermediary serviceProvider) {
        Map<String, Object> properties = DbCaster.toProperties(serviceProvider);
        this.jdbcCaller.call(this.createNewProc, properties);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(Long id) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_cst_id", id);
        this.jdbcCaller.call(this.deleteProc, properties);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void update(Intermediary operator) {
        Map<String, Object> properties = DbCaster.toProperties(operator);
        this.jdbcCaller.call(this.updateProc, properties);
    }
}
