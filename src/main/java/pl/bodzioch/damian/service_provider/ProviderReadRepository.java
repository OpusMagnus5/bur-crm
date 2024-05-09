package pl.bodzioch.damian.service_provider;

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
class ProviderReadRepository implements IProviderReadRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall getByNipProc;
    private final SimpleJdbcCall getPageProc;
    private final SimpleJdbcCall getDetailsProc;

    public ProviderReadRepository(IJdbcCaller jdbcCaller, DataSource dataSource) {
        this.jdbcCaller = jdbcCaller;
        this.getByNipProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "service_provider_get_by_nip");
        this.getPageProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "service_provider_get_page");
        this.getDetailsProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "service_provider_get_details");
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<ServiceProvider> getByNip(Long nip) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_spr_nip", nip);
        getByNipProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getByNipProc, properties);
        return DbCaster.fromProperties(result, ServiceProvider.class).stream().findFirst();
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public PageQueryResult<ServiceProvider> getPage(PageQuery pageQuery) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_offset", pageQuery.getFirstResult());
        properties.put("_max", pageQuery.getMaxResult());

        getPageProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR),
                new SqlOutParameter("_total_providers", Types.BIGINT));
        Map<String, Object> result = jdbcCaller.call(getPageProc, properties);

        Long totalProviders = (Long) result.get("_total_providers");
        List<ServiceProvider> serviceProviders = DbCaster.fromProperties(result, ServiceProvider.class);
        return new PageQueryResult<>(serviceProviders, totalProviders);
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<ServiceProvider> getDetails(Long id) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_spr_id", id);
        getDetailsProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getDetailsProc, properties);
        return DbCaster.fromProperties(result, ServiceProvider.class).stream().findFirst();
    }
}
