package pl.bodzioch.damian.program;

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
class ProgramReadRepository implements IProgramReadRepository {

    private final IJdbcCaller jdbcCaller;
    private final SimpleJdbcCall getPageProc;
    private final SimpleJdbcCall getByNameProc;
    private final SimpleJdbcCall getDetailsProc;
    private final SimpleJdbcCall getAllProc;

    public ProgramReadRepository(IJdbcCaller jdbcCaller) {
        this.jdbcCaller = jdbcCaller;
        this.getPageProc = jdbcCaller.buildSimpleJdbcCall("program_get_page");
        this.getByNameProc = jdbcCaller.buildSimpleJdbcCall("program_get_by_name_and_operator_id");
        this.getDetailsProc = jdbcCaller.buildSimpleJdbcCall("program_get_details");
        this.getAllProc = jdbcCaller.buildSimpleJdbcCall("program_get_all");
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public PageQueryResult<Program> getPage(PageQuery pageQuery) {
        HashMap<String, Object> properties = jdbcCaller.buildPageParams(pageQuery);

        getPageProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR),
                new SqlOutParameter("_total_programs", Types.BIGINT));
        Map<String, Object> result = jdbcCaller.call(getPageProc, properties);

        Long totalPrograms = (Long) result.get("_total_programs");
        List<Program> serviceProviders = DbCaster.fromProperties(result, Program.class);
        return new PageQueryResult<>(serviceProviders, totalPrograms);
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<Program> get(String name, Long operatorId) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_prg_name", name);
        properties.put("_prg_operator_id", operatorId);
        getByNameProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getByNameProc, properties);
        return DbCaster.fromProperties(result, Program.class).stream().findFirst();
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<Program> getDetails(Long id) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("_prg_id", id);
        getDetailsProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getDetailsProc, properties);
        return DbCaster.fromProperties(result, Program.class).stream().findFirst();
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public List<Program> getAll() {
        HashMap<String, Object> properties = new HashMap<>();
        getAllProc.declareParameters(new SqlOutParameter(GENERAL_CURSOR_NAME, Types.REF_CURSOR));
        Map<String, Object> result = jdbcCaller.call(getAllProc, properties);
        return DbCaster.fromProperties(result, Program.class);
    }
}
