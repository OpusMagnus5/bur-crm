package pl.bodzioch.damian.service_provider;

import jakarta.transaction.Transactional;

public interface IProviderWriteRepository {
    @Transactional(Transactional.TxType.REQUIRED)
    void createNew(ServiceProvider serviceProvider);
}
