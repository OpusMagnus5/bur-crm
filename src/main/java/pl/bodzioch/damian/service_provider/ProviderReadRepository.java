package pl.bodzioch.damian.service_provider;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.DbCaster;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

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
    private final SimpleJdbcCall getAll;
    private final SimpleJdbcCall getByBurIdProc;

    public ProviderReadRepository(IJdbcCaller jdbcCaller) {
        this.jdbcCaller = jdbcCaller;
        this.getByNipProc = jdbcCaller.buildSimpleJdbcCall("service_provider_get_by_nip");
        this.getPageProc = jdbcCaller.buildSimpleJdbcCall("service_provider_get_page");
        this.getDetailsProc = jdbcCaller.buildSimpleJdbcCall("service_provider_get_details");
        this.getAll = jdbcCaller.buildSimpleJdbcCall("service_provider_get_all");
        this.getByBurIdProc = jdbcCaller.buildSimpleJdbcCall("service_provider_get_by_bur_id");
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
        HashMap<String, Object> properties = jdbcCaller.buildPageParams(pageQuery);

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

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public List<ServiceProvider> getAll() {
        getAll.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getAll, new HashMap<>());
        return DbCaster.fromProperties(result, ServiceProvider.class);
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<ServiceProvider> getByBurId(Long burId) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_spr_bur_id", burId);
        getByBurIdProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getByBurIdProc, properties);
        return DbCaster.fromProperties(result, ServiceProvider.class).stream().findFirst();
    }
}
