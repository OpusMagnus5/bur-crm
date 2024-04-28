package pl.bodzioch.damian.user;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.valueobject.PageQuery;
import pl.bodzioch.damian.valueobject.PageQueryResult;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
class UserReadRepository implements IUserReadRepository {

    private final SimpleJdbcCall getByEmailProc;
    private final SimpleJdbcCall getPageOfUsersProc;
    private final SimpleJdbcCall getDetailsProc;

    public UserReadRepository(DataSource dataSource) {
        this.getByEmailProc = buildSimpleJdbcCall(dataSource, "users_get_by_email");
        this.getPageOfUsersProc = buildSimpleJdbcCall(dataSource, "users_get_page_of_users");
        this.getDetailsProc  = buildSimpleJdbcCall(dataSource, "users_get_details");
    }

    private SimpleJdbcCall buildSimpleJdbcCall(DataSource dataSource, String procedure) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        return new SimpleJdbcCall(jdbcTemplate).withProcedureName(procedure);
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<User> getByEmail(String email) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_usr_email", email);
        Map<String, Object> result = getByEmailProc.declareParameters(new SqlOutParameter("_cursor", Types.REF_CURSOR))
                .execute(properties);
        return User.fromProperties(result, "_cursor").stream().findFirst();
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public PageQueryResult<User> getUsers(PageQuery pageQuery) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_offset", pageQuery.getFirstResult());
        properties.put("_max", pageQuery.getMaxResult());
        Map<String, Object> result = getPageOfUsersProc.declareParameters(new SqlOutParameter("_cursor", Types.REF_CURSOR))
                .declareParameters(new SqlOutParameter("_total_users", Types.BIGINT))
                .execute(properties);
        Long totalUsers = (Long) result.get("_total_users");
        List<User> users = User.fromProperties(result, "_cursor");
        return new PageQueryResult<>(users, totalUsers);
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<User> getById(Long id) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_usr_id", id);
        Map<String, Object> result = getDetailsProc.declareParameters(new SqlOutParameter("_cursor", Types.REF_CURSOR))
                .execute(properties);
        return User.fromProperties(result, "_cursor").stream().findFirst();
    }
}
