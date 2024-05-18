package pl.bodzioch.damian.coach;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.DbCaster;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import javax.sql.DataSource;
import java.util.Map;

@Repository
class CoachWriteRepository implements ICoachWriteRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall createNewProc;

    CoachWriteRepository(IJdbcCaller jdbcCaller, DataSource dataSource) {
        this.jdbcCaller = jdbcCaller;
        this.createNewProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "coach_create_new");
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createNew(Coach coach) {
        Map<String, Object> properties = DbCaster.toProperties(coach);
        this.jdbcCaller.call(this.createNewProc, properties);
    }
}
