package pl.bodzioch.damian.user;

import jakarta.transaction.Transactional;

import java.util.List;

interface IUserWriteRepository {

    @Transactional(Transactional.TxType.REQUIRED)
    void createNew(User user);

    @Transactional(Transactional.TxType.REQUIRED)
    void delete(Long id);

    @Transactional(Transactional.TxType.REQUIRED)
    void changePassword(User user);

    @Transactional(Transactional.TxType.REQUIRED)
    void setLastLogin(Long userId);

    @Transactional(Transactional.TxType.REQUIRED)
    void createSystemUsers(List<User> user);
}
