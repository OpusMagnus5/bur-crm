package pl.bodzioch.damian.service_provider;

import jakarta.transaction.Transactional;

import java.util.Optional;

interface IProviderReadRepository {
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Optional<ServiceProvider> getByNip(Long nip);
}
