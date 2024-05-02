package pl.bodzioch.damian.service_provider;

import jakarta.transaction.Transactional;

interface IProviderWriteRepository {
    @Transactional(Transactional.TxType.REQUIRED)
    void createNew(ServiceProvider serviceProvider);

    @Transactional(Transactional.TxType.REQUIRED)
    void delete(Long id);
}
