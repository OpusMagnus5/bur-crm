package pl.bodzioch.damian.coach;

import jakarta.transaction.Transactional;

import java.util.Optional;

interface ICoachReadRepository {
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Optional<Coach> getByNip(String pesel);
}
