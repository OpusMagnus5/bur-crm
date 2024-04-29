package pl.bodzioch.damian.user;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
class UserReadRepository implements IUserReadRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall getByEmailProc;
    private final SimpleJdbcCall getPageOfUsersProc;
    private final SimpleJdbcCall getDetailsProc;

    UserReadRepository(DataSource dataSource, IJdbcCaller jdbcCaller) {
        this.jdbcCaller = jdbcCaller;
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
        getByEmailProc.declareParameters(new SqlOutParameter("_cursor", Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getByEmailProc, properties);
        return User.fromProperties(result, "_cursor").stream().findFirst();
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public PageQueryResult<User> getUsers(PageQuery pageQuery) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_offset", pageQuery.getFirstResult());
        properties.put("_max", pageQuery.getMaxResult());

        getPageOfUsersProc.declareParameters(new SqlOutParameter("_cursor", Types.REF_CURSOR))
                .declareParameters(new SqlOutParameter("_total_users", Types.BIGINT));
        Map<String, Object> result = jdbcCaller.call(getPageOfUsersProc, properties);

        Long totalUsers = (Long) result.get("_total_users");
        List<User> users = User.fromProperties(result, "_cursor");
        return new PageQueryResult<>(users, totalUsers);
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<User> getById(Long id) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_usr_id", id);
        getDetailsProc.declareParameters(new SqlOutParameter("_cursor", Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getDetailsProc, properties);
        return User.fromProperties(result, "_cursor").stream().findFirst();
    }
}
