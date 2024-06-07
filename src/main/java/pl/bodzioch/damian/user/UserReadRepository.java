package pl.bodzioch.damian.user;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.DbCaster;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static pl.bodzioch.damian.infrastructure.database.DbCaster.GENERAL_CURSOR_NAME;

@Repository
class UserReadRepository implements IUserReadRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall getByEmailProc;
    private final SimpleJdbcCall getPageOfUsersProc;
    private final SimpleJdbcCall getDetailsProc;

    UserReadRepository(IJdbcCaller jdbcCaller) {
        this.jdbcCaller = jdbcCaller;
        this.getByEmailProc = jdbcCaller.buildSimpleJdbcCall("users_get_by_email");
        this.getPageOfUsersProc = jdbcCaller.buildSimpleJdbcCall("users_get_page_of_users");
        this.getDetailsProc  = jdbcCaller.buildSimpleJdbcCall("users_get_details");
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<User> getByEmail(String email) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_usr_email", email);
        getByEmailProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getByEmailProc, properties);
        return DbCaster.fromProperties(result, User.class).stream().findFirst();
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public PageQueryResult<User> getUsers(PageQuery pageQuery) {
        HashMap<String, Object> properties = jdbcCaller.buildPageParams(pageQuery);

        getPageOfUsersProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR))
                .declareParameters(new SqlOutParameter("_total_users", Types.BIGINT));
        Map<String, Object> result = jdbcCaller.call(getPageOfUsersProc, properties);

        Long totalUsers = (Long) result.get("_total_users");
        List<User> users = DbCaster.fromProperties(result,  User.class);
        return new PageQueryResult<>(users, totalUsers);
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<User> getById(Long id) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_usr_id", id);
        getDetailsProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getDetailsProc, properties);
        return DbCaster.fromProperties(result, User.class).stream().findFirst();
    }
}
