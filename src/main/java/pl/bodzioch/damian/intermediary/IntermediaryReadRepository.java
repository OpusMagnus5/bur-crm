package pl.bodzioch.damian.intermediary;

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
class IntermediaryReadRepository implements IIntermediaryReadRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall getByNipProc;
    private final SimpleJdbcCall getPageProc;
    private final SimpleJdbcCall getDetailsProc;

    IntermediaryReadRepository(IJdbcCaller jdbcCaller, DataSource dataSource) {
        this.jdbcCaller = jdbcCaller;
        this.getByNipProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "intermediary_get_by_nip");
        this.getPageProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "intermediary_get_page");
        this.getDetailsProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "intermediary_get_details");
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<Intermediary> getByNip(Long nip) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_cst_nip", nip);
        getByNipProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getByNipProc, properties);
        return DbCaster.fromProperties(result, Intermediary.class).stream().findFirst();
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public PageQueryResult<Intermediary> getPage(PageQuery pageQuery) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_offset", pageQuery.getFirstResult());
        properties.put("_max", pageQuery.getMaxResult());
        properties.putAll(pageQuery.filtersToDbProperties());

        getPageProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR),
                new SqlOutParameter("_total_providers", Types.BIGINT));
        Map<String, Object> result = jdbcCaller.call(getPageProc, properties);

        Long totalProviders = (Long) result.get("_total_intermediaries");
        List<Intermediary> serviceProviders = DbCaster.fromProperties(result, Intermediary.class);
        return new PageQueryResult<>(serviceProviders, totalProviders);
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<Intermediary> getDetails(Long id) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_cst_id", id);
        getDetailsProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getDetailsProc, properties);
        return DbCaster.fromProperties(result, Intermediary.class).stream().findFirst();
    }
}
