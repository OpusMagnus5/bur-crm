package pl.bodzioch.damian.service_provider;

import jakarta.transaction.Transactional;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import java.util.List;
import java.util.Optional;

interface IProviderReadRepository {
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Optional<ServiceProvider> getByNip(Long nip);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    PageQueryResult<ServiceProvider> getPage(PageQuery pageQuery);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Optional<ServiceProvider> getDetails(Long id);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    List<ServiceProvider> getAll();
}
