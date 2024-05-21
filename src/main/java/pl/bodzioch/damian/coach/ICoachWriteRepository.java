package pl.bodzioch.damian.coach;

import jakarta.transaction.Transactional;

interface ICoachWriteRepository {
    @Transactional(Transactional.TxType.REQUIRED)
    void createNew(Coach coach);

    @Transactional(Transactional.TxType.REQUIRED)
    void delete(Long id);

    @Transactional(Transactional.TxType.REQUIRED)
    void update(Coach operator);
}
