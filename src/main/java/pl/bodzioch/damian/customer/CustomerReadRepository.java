package pl.bodzioch.damian.customer;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.DbCaster;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static pl.bodzioch.damian.infrastructure.database.DbCaster.GENERAL_CURSOR_NAME;

@Repository
class CustomerReadRepository implements ICustomerReadRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall getByNipProc;

    CustomerReadRepository(IJdbcCaller jdbcCaller, DataSource dataSource) {
        this.jdbcCaller = jdbcCaller;
        this.getByNipProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "customer_get_by_nip");
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<Customer> getByNip(Long nip) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_cst_nip", nip);
        getByNipProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getByNipProc, properties);
        return DbCaster.fromProperties(result, Customer.class).stream().findFirst();
    }
}
