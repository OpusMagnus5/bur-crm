package pl.bodzioch.damian.coach;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.infrastructure.database.DbCaster;
import pl.bodzioch.damian.infrastructure.database.IJdbcCaller;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static pl.bodzioch.damian.infrastructure.database.DbCaster.GENERAL_CURSOR_NAME;

@Repository
class CoachReadRepository implements ICoachReadRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall getByPeselProc;

    CoachReadRepository(IJdbcCaller jdbcCaller, DataSource dataSource) {
        this.jdbcCaller = jdbcCaller;
        this.getByPeselProc = jdbcCaller.buildSimpleJdbcCall(dataSource, "coach_get_by_nip");
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<Coach> getByNip(String pesel) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_coa_pesel", pesel);
        getByPeselProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getByPeselProc, properties);
        return DbCaster.fromProperties(result, Coach.class).stream().findFirst();
    }
}
