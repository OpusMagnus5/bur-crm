package pl.bodzioch.damian.coach;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.DbCaster;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import java.util.HashMap;
import java.util.Map;

@Repository
class CoachWriteRepository implements ICoachWriteRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall createNewProc;
    private final SimpleJdbcCall deleteProc;
    private final SimpleJdbcCall updateProc;

    CoachWriteRepository(IJdbcCaller jdbcCaller) {
        this.jdbcCaller = jdbcCaller;
        this.createNewProc = jdbcCaller.buildSimpleJdbcCall("coach_create_new");
        this.deleteProc = jdbcCaller.buildSimpleJdbcCall("coach_delete");
        this.updateProc = jdbcCaller.buildSimpleJdbcCall("coach_update");
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createNew(Coach coach) {
        Map<String, Object> properties = DbCaster.toProperties(coach);
        this.jdbcCaller.call(this.createNewProc, properties);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(Long id) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_coa_id", id);
        this.jdbcCaller.call(this.deleteProc, properties);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void update(Coach operator) {
        Map<String, Object> properties = DbCaster.toProperties(operator);
        this.jdbcCaller.call(this.updateProc, properties);
    }
}
