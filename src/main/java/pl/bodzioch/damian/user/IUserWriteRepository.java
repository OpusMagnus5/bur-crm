package pl.bodzioch.damian.user;

import jakarta.transaction.Transactional;

interface IUserWriteRepository {

    @Transactional(Transactional.TxType.REQUIRED)
    User createNew(UserEntityWrite userEntityWrite);
}
