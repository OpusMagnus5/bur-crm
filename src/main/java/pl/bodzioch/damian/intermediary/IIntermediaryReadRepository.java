package pl.bodzioch.damian.intermediary;

import jakarta.transaction.Transactional;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import java.util.List;
import java.util.Optional;

interface IIntermediaryReadRepository {
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Optional<Intermediary> getByNip(Long nip);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    PageQueryResult<Intermediary> getPage(PageQuery pageQuery);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Optional<Intermediary> getDetails(Long id);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    List<Intermediary> getAll();
}
