package pl.bodzioch.damian.program;

import jakarta.transaction.Transactional;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import java.util.Optional;

interface IProgramReadRepository {
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    PageQueryResult<Program> getPage(PageQuery pageQuery);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Optional<Program> get(String name, Long operatorId);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Optional<Program> getDetails(Long id);
}
