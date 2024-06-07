package pl.bodzioch.damian.customer;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.DbCaster;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import java.util.HashMap;
import java.util.Map;

@Repository
class CustomerWriteRepository implements ICustomerWriteRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall createNewProc;
    private final SimpleJdbcCall deleteProc;
    private final SimpleJdbcCall updateProc;

    CustomerWriteRepository(IJdbcCaller jdbcCaller) {
        this.jdbcCaller = jdbcCaller;
        this.createNewProc = jdbcCaller.buildSimpleJdbcCall("customer_create_new");
        this.deleteProc = jdbcCaller.buildSimpleJdbcCall("customer_delete");
        this.updateProc = jdbcCaller.buildSimpleJdbcCall("customer_update");

    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createNew(Customer serviceProvider) {
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
    public void update(Customer operator) {
        Map<String, Object> properties = DbCaster.toProperties(operator);
        this.jdbcCaller.call(this.updateProc, properties);
    }
}
