package pl.bodzioch.damian.customer;

import jakarta.transaction.Transactional;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import java.util.Optional;

interface ICustomerReadRepository {
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Optional<Customer> getByNip(Long nip);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    PageQueryResult<Customer> getPage(PageQuery pageQuery);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Optional<Customer> getDetails(Long id);
}
