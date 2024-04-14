package pl.bodzioch.damian.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
class UserWriteRepository implements IUserWriteRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public User createNew(UserEntity userEntity) {
        log.info("Creating new user: {}", userEntity);
        entityManager.persist(userEntity);
        entityManager.flush();
        entityManager.clear();
        log.info("Created new user: {}", userEntity);
        return userEntity.toUser();
    }
}
