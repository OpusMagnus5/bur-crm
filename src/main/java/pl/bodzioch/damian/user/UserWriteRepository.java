package pl.bodzioch.damian.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
class UserWriteRepository implements IUserWriteRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public User createNew(UserEntityWrite userEntityWrite) {
        log.info("Creating new user: {}", userEntityWrite);
        entityManager.persist(userEntityWrite);
        entityManager.flush();
        entityManager.clear();
        log.info("Created new user: {}", userEntityWrite);
        return userEntityWrite.toUser();
    }
}
