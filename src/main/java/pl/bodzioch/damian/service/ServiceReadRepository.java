package pl.bodzioch.damian.service;

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
class ServiceReadRepository implements IServiceReadRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall getPageProc;
    private final SimpleJdbcCall getDetailsProc;

    ServiceReadRepository(IJdbcCaller jdbcCaller, DataSource dataSource) {
        this.jdbcCaller = jdbcCaller;
        this.getPageProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "service_get_page");
        this.getDetailsProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "service_get_details");
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public PageQueryResult<Service> getPage(PageQuery pageQuery) {
        HashMap<String, Object> properties = jdbcCaller.buildPageParams(pageQuery);

        getPageProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR),
                new SqlOutParameter("_total_services", Types.BIGINT));
        Map<String, Object> result = jdbcCaller.call(getPageProc, properties);

        Long totalPrograms = (Long) result.get("_total_services");
        List<Service> serviceProviders = DbCaster.fromProperties(result, Service.class);
        return new PageQueryResult<>(serviceProviders, totalPrograms);
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<Service> getDetails(Long id) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_srv_id", id);
        getDetailsProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getDetailsProc, properties);
        return DbCaster.fromProperties(result, Service.class).stream().findFirst();
    }
}
