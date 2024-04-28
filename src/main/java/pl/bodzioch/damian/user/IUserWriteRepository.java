package pl.bodzioch.damian.user;

import jakarta.transaction.Transactional;

interface IUserWriteRepository {

    @Transactional(Transactional.TxType.REQUIRED)
    void createNew(User user);

    @Transactional(Transactional.TxType.REQUIRED)
    void delete(Long id);
}
