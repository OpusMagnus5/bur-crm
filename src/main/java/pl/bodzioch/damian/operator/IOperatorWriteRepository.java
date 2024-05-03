package pl.bodzioch.damian.operator;

import jakarta.transaction.Transactional;

interface IOperatorWriteRepository {
    @Transactional(Transactional.TxType.REQUIRED)
    void createNew(Operator operator);
}
