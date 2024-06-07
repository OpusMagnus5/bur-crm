package pl.bodzioch.damian.service;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.DbCaster;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import java.sql.Types;
import java.util.List;
import java.util.Map;

@Repository
class ServiceWriteRepository implements IServiceWriteRepository {

	private final IJdbcCaller jdbcCaller;
	private final SimpleJdbcCall createNewProc;
	private final SimpleJdbcCall updateStatus;

	ServiceWriteRepository(IJdbcCaller jdbcCaller) {
		this.jdbcCaller = jdbcCaller;
		this.createNewProc = jdbcCaller.buildSimpleJdbcCall("service_create_or_update");
		this.updateStatus = jdbcCaller.buildSimpleJdbcCall("service_update_status");
	}

	@Override
	@Transactional(Transactional.TxType.REQUIRED)
	public void createOrUpdate(Service service) {
		Map<String, Object> properties = DbCaster.toProperties(service);
		this.jdbcCaller.call(this.createNewProc, properties);
	}

	@Override
	@Transactional(Transactional.TxType.REQUIRED)
	public void updateStatus(List<Service> services) {
		Map<String, Object> properties = Map.of("_service_status_data", "{\"(1,CACY)\",\"(3,CACY)\"}");
		this.updateStatus.declareParameters(new SqlParameter("_service_status_data", Types.OTHER));
		this.jdbcCaller.call(this.updateStatus, properties);
	}
}
