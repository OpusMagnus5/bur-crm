package pl.bodzioch.damian.program;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.DbCaster;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import javax.sql.DataSource;
import java.util.Map;

@Repository
class ProgramWriteRepository implements IProgramWriteRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall createNewProc;

    ProgramWriteRepository(IJdbcCaller jdbcCaller, DataSource dataSource) {
        this.jdbcCaller = jdbcCaller;
        this.createNewProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "program_create_new");
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createNew(Program program) {
        Map<String, Object> properties = DbCaster.toProperties(program);
        this.jdbcCaller.call(this.createNewProc, properties);
    }
}
