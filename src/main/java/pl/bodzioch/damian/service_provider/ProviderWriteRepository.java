package pl.bodzioch.damian.service_provider;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.DbCaster;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
class ProviderWriteRepository implements IProviderWriteRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall createNewProc;
    private final SimpleJdbcCall deleteProc;
    private final SimpleJdbcCall updateProc;

    ProviderWriteRepository(IJdbcCaller jdbcCaller, DataSource dataSource) {
        this.jdbcCaller = jdbcCaller;
        this.createNewProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "service_provider_create_new");
        this.deleteProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "service_provider_delete");
        this.updateProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "service_provider_update");
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createNew(ServiceProvider serviceProvider) {
        Map<String, Object> properties = DbCaster.toProperties(serviceProvider);
        this.jdbcCaller.call(this.createNewProc, properties);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(Long id) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_spr_id", id);
        this.jdbcCaller.call(this.deleteProc, properties);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void update(ServiceProvider serviceProvider) {
        Map<String, Object> properties = DbCaster.toProperties(serviceProvider);
        this.jdbcCaller.call(this.updateProc, properties);
    }
}
