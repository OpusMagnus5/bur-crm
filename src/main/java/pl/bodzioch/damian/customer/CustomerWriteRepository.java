package pl.bodzioch.damian.customer;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.DbCaster;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import javax.sql.DataSource;
import java.util.Map;

@Repository
class CustomerWriteRepository implements ICustomerWriteRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall createNewProc;

    CustomerWriteRepository(IJdbcCaller jdbcCaller, DataSource dataSource) {
        this.jdbcCaller = jdbcCaller;
        this.createNewProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "customer_create_new");
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createNew(Customer serviceProvider) {
        Map<String, Object> properties = DbCaster.toProperties(serviceProvider);
        this.jdbcCaller.call(this.createNewProc, properties);
    }
}
