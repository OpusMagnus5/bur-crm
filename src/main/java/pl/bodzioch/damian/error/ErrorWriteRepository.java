package pl.bodzioch.damian.error;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.DbCaster;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import java.util.Map;

@Repository
class ErrorWriteRepository implements IErrorWriteRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall addNewErrorProc;

    ErrorWriteRepository(IJdbcCaller jdbcCaller) {
        this.jdbcCaller = jdbcCaller;
        this.addNewErrorProc = jdbcCaller.buildSimpleJdbcCall("error_add_new");
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void saveError(Error error) {
        Map<String, Object> properties = DbCaster.toProperties(error);
        this.jdbcCaller.call(this.addNewErrorProc, properties);
    }
}
