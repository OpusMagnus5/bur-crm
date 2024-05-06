package pl.bodzioch.damian.operator;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
class OperatorReadRepository implements IOperatorReadRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall getByNameProc;
    private final SimpleJdbcCall getPageProc;

    public OperatorReadRepository(IJdbcCaller jdbcCaller, DataSource dataSource) {
        this.jdbcCaller = jdbcCaller;
        this.getByNameProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "operator_get_by_name");
        this.getPageProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "operator_get_page");
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<Operator> getByName(String name) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_opr_name", name);
        getByNameProc.declareParameters(new SqlOutParameter("_cursor", Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getByNameProc, properties);
        return Operator.fromProperties(result, "_cursor").stream().findFirst();
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public PageQueryResult<Operator> getPage(PageQuery pageQuery) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_offset", pageQuery.getFirstResult());
        properties.put("_max", pageQuery.getMaxResult());
        properties.putAll(pageQuery.toDbProperties());

        getPageProc.declareParameters(new SqlOutParameter("_cursor", Types.REF_CURSOR),
                new SqlOutParameter("_total_operators", Types.BIGINT));
        Map<String, Object> result = jdbcCaller.call(getPageProc, properties);

        Long totalOperators = (Long) result.get("_total_operators");
        List<Operator> serviceProviders = Operator.fromProperties(result, "_cursor");
        return new PageQueryResult<>(serviceProviders, totalOperators);
    }
}
