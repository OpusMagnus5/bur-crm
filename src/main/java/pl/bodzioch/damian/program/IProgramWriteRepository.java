package pl.bodzioch.damian.program;

import jakarta.transaction.Transactional;

interface IProgramWriteRepository {
    @Transactional(Transactional.TxType.REQUIRED)
    void createNew(Program program);
}