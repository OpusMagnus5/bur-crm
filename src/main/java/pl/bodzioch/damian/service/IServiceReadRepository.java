package pl.bodzioch.damian.service;

import jakarta.transaction.Transactional;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import java.util.List;
import java.util.Optional;

interface IServiceReadRepository {
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    PageQueryResult<Service> getPage(PageQuery pageQuery);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Optional<Service> getDetails(Long id);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    List<Service> getServicesToStatusCheck();
}
