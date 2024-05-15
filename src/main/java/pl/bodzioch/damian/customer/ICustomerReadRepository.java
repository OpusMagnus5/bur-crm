package pl.bodzioch.damian.customer;

import jakarta.transaction.Transactional;

import java.util.Optional;

interface ICustomerReadRepository {
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Optional<Customer> getByNip(Long nip);
}
