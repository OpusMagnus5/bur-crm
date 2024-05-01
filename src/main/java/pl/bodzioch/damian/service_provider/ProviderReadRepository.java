package pl.bodzioch.damian.service_provider;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
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
class ProviderReadRepository implements IProviderReadRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall getByNipProc;

    public ProviderReadRepository(IJdbcCaller jdbcCaller, DataSource dataSource) {
        this.jdbcCaller = jdbcCaller;
        this.getByNipProc = buildSimpleJdbcCall(dataSource, "service_provider_get_by_nip");
    }

    private SimpleJdbcCall buildSimpleJdbcCall(DataSource dataSource, String procedure) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        return new SimpleJdbcCall(jdbcTemplate).withProcedureName(procedure);
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<ServiceProvider> getByNip(Long nip) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_spr_nip", nip);
        getByNipProc.declareParameters(new SqlOutParameter("_cursor", Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getByNipProc, properties);
        return ServiceProvider.fromProperties(result, "_cursor").stream().findFirst();
    }
}
