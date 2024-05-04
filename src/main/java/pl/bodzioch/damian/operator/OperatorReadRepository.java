package pl.bodzioch.damian.operator;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
class OperatorReadRepository implements IOperatorReadRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall getByName;

    public OperatorReadRepository(IJdbcCaller jdbcCaller, DataSource dataSource) {
        this.jdbcCaller = jdbcCaller;
        this.getByName = jdbcCaller.buildSimpleJdbcCall(dataSource, "operator_get_by_name");
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<Operator> getByName(String name) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_opr_name", name);
        getByName.declareParameters(new SqlOutParameter("_cursor", Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getByName, properties);
        return Operator.fromProperties(result, "_cursor").stream().findFirst();
    }
}
