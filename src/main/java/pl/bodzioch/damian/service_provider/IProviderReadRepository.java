package pl.bodzioch.damian.service_provider;

import jakarta.transaction.Transactional;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import java.util.Optional;

interface IProviderReadRepository {
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Optional<ServiceProvider> getByNip(Long nip);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    PageQueryResult<ServiceProvider> getPage(PageQuery pageQuery);
}
