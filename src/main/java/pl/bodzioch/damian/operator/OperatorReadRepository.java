package pl.bodzioch.damian.operator;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.DbCaster;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static pl.bodzioch.damian.infrastructure.database.DbCaster.GENERAL_CURSOR_NAME;

@Repository
class OperatorReadRepository implements IOperatorReadRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall getByNameProc;
    private final SimpleJdbcCall getPageProc;
    private final SimpleJdbcCall getDetailsProc;
    private final SimpleJdbcCall getAllProc;

    public OperatorReadRepository(IJdbcCaller jdbcCaller, DataSource dataSource) {
        this.jdbcCaller = jdbcCaller;
        this.getByNameProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "operator_get_by_name");
        this.getPageProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "operator_get_page");
        this.getDetailsProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "operator_get_details");
        this.getAllProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "operator_get_all");

    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<Operator> getByName(String name) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_opr_name", name);
        getByNameProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getByNameProc, properties);
        return DbCaster.fromProperties(result, Operator.class).stream().findFirst();
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public PageQueryResult<Operator> getPage(PageQuery pageQuery) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_offset", pageQuery.getFirstResult());
        properties.put("_max", pageQuery.getMaxResult());
        properties.putAll(pageQuery.filtersToDbProperties());

        getPageProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR),
                new SqlOutParameter("_total_operators", Types.BIGINT));
        Map<String, Object> result = jdbcCaller.call(getPageProc, properties);

        Long totalOperators = (Long) result.get("_total_operators");
        List<Operator> serviceProviders = DbCaster.fromProperties(result, Operator.class);
        return new PageQueryResult<>(serviceProviders, totalOperators);
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<Operator> getDetails(Long id) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_opr_id", id);
        getDetailsProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getDetailsProc, properties);
        return DbCaster.fromProperties(result, Operator.class).stream().findFirst();
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public List<Operator> getAll() {
        getAllProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getAllProc, new HashMap<>());
        return DbCaster.fromProperties(result, Operator.class);
    }
}
