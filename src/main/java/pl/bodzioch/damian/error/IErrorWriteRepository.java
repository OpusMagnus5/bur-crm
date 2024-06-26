package pl.bodzioch.damian.error;

import jakarta.transaction.Transactional;

interface IErrorWriteRepository {
    @Transactional(Transactional.TxType.REQUIRED)
    void saveError(Error error);
}
