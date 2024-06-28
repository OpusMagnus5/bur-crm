package pl.bodzioch.damian.user;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.DbCaster;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import java.util.HashMap;
import java.util.Map;

@Repository
class UserWriteRepository implements IUserWriteRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall createNewProc;
    private final SimpleJdbcCall deleteProc;
    private final SimpleJdbcCall changePasswordProc;
    private final SimpleJdbcCall setLastLoginProc;

    UserWriteRepository(IJdbcCaller jdbcCaller) {
        this.jdbcCaller = jdbcCaller;
        this.createNewProc = jdbcCaller.buildSimpleJdbcCall("users_create_new_or_update");
        this.deleteProc = jdbcCaller.buildSimpleJdbcCall("users_delete");
        this.changePasswordProc = jdbcCaller.buildSimpleJdbcCall("users_change_password");
        this.setLastLoginProc = jdbcCaller.buildSimpleJdbcCall("users_set_last_login");
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createNew(User user) {
        Map<String, Object> properties = DbCaster.toProperties(user);
        this.jdbcCaller.call(this.createNewProc, properties);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(Long id) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_usr_id", id);
        this.jdbcCaller.call(this.deleteProc, properties);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void changePassword(User user) {
        Map<String, Object> properties = DbCaster.toProperties(user);
        this.jdbcCaller.call(this.changePasswordProc, properties);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void setLastLogin(Long userId) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_usr_id", userId);
        this.jdbcCaller.call(this.setLastLoginProc, properties);
    }
}
