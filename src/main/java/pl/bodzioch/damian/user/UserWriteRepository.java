package pl.bodzioch.damian.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.exception.AppException;

@Slf4j
@Repository
class UserWriteRepository implements IUserWriteRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createNew(UserEntityWrite userEntityWrite) {
        log.info("Creating new user: {}", userEntityWrite);
        entityManager.persist(userEntityWrite);
        entityManager.flush();
        entityManager.clear();
        log.info("Created new user: {}", userEntityWrite);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(Long id) {
        log.info("Deleting user with id: {}", id);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaDelete<UserEntityWrite> query = builder.createCriteriaDelete(UserEntityWrite.class);
        Root<UserEntityWrite> root = query.from(UserEntityWrite.class);
        Predicate idPredicate = builder.equal(root.get("id"), id);
        query.where(idPredicate);
        int result = entityManager.createQuery(query).executeUpdate();
        log.info("Result deleting user with id: {} is: {}", id, result);
        if (result != 1) {
            throw new AppException("Error occurred while deleting user");
        }
    }
}
