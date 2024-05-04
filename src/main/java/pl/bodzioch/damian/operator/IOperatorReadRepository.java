package pl.bodzioch.damian.operator;

import jakarta.transaction.Transactional;

import java.util.Optional;

interface IOperatorReadRepository {
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Optional<Operator> getByName(String name);
}
