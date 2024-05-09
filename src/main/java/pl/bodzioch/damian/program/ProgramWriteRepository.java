package pl.bodzioch.damian.program;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import javax.sql.DataSource;

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
        this.jdbcCaller.call(this.createNewProc, program.getPropertySource());
    }

}
