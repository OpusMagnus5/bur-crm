package pl.bodzioch.damian.service_provider;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import javax.sql.DataSource;

@Repository
class ProviderWriteRepository implements IProviderWriteRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall createNewProc;

    ProviderWriteRepository(IJdbcCaller jdbcCaller, DataSource dataSource) {
        this.jdbcCaller = jdbcCaller;
        this.createNewProc = buildSimpleJdbcCall(dataSource, "service_provider_create_new");
    }

    private SimpleJdbcCall buildSimpleJdbcCall(DataSource dataSource, String procedure) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        return new SimpleJdbcCall(jdbcTemplate).withProcedureName(procedure);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createNew(ServiceProvider serviceProvider) {
        this.jdbcCaller.call(this.createNewProc, serviceProvider.getPropertySource());
    }
}
