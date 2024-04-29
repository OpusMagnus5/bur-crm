package pl.bodzioch.damian.user;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;

@Slf4j
@Repository
class UserWriteRepository implements IUserWriteRepository {

    private final SimpleJdbcCall createNewProc;
    private final SimpleJdbcCall deleteProc;

    public UserWriteRepository(DataSource dataSource) {
        this.createNewProc = buildSimpleJdbcCall(dataSource, "users_create_new");
        this.deleteProc = buildSimpleJdbcCall(dataSource, "users_delete");
    }

    private SimpleJdbcCall buildSimpleJdbcCall(DataSource dataSource, String procedure) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        return new SimpleJdbcCall(jdbcTemplate).withProcedureName(procedure);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createNew(User user) {
        this.createNewProc.execute(user.getPropertySource());
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(Long id) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_usr_id", id);
        this.deleteProc.execute(properties);
    }
}
