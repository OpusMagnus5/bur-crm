package pl.bodzioch.damian.program;

import jakarta.transaction.Transactional;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

interface IProgramReadRepository {
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    PageQueryResult<Program> getPage(PageQuery pageQuery);
}
