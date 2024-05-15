package pl.bodzioch.damian.customer;

import jakarta.transaction.Transactional;

interface ICustomerWriteRepository {
    @Transactional(Transactional.TxType.REQUIRED)
    void createNew(Customer serviceProvider);
}
