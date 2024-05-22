package pl.bodzioch.damian.service;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.DbCaster;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import javax.sql.DataSource;
import java.util.Map;

@Repository
class ServiceWriteRepository implements IServiceWriteRepository {

	private final IJdbcCaller jdbcCaller;
	private final SimpleJdbcCall createNewProc;

	ServiceWriteRepository(IJdbcCaller jdbcCaller, DataSource dataSource) {
		this.jdbcCaller = jdbcCaller;
		this.createNewProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "service_create_new");
	}

	@Override
	@Transactional(Transactional.TxType.REQUIRED)
	public void createNew(Service service) {
		Map<String, Object> properties = DbCaster.toProperties(service);
		this.jdbcCaller.call(this.createNewProc, properties);
	}
}
