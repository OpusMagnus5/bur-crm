package pl.bodzioch.damian.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import pl.bodzioch.damian.valueobject.PageQuery;
import pl.bodzioch.damian.valueobject.PageQueryResult;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
class UserReadRepository implements IUserReadRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<User> getByEmail(String email) {
        log.info("Search user by email: {}", email);
        Session session = entityManager.unwrap(Session.class);

        Optional<UserEntity> user = session.byNaturalId(UserEntity.class).using("email", email).loadOptional();
        if (user.isPresent()) {
            log.info("User with the email: {} found: {}", email, user.get());
        } else {
            log.info("User with the email: {} NOT found", email);
        }
        session.clear();
        return user.map(UserEntity::toUser);
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public PageQueryResult<User> getUsers(PageQuery pageQuery) {
        log.info("Start search all users");
        CriteriaQuery<UserEntity> queryBuilder = entityManager.getCriteriaBuilder().createQuery(UserEntity.class);
        queryBuilder.from(UserEntity.class);
        TypedQuery<UserEntity> query = entityManager.createQuery(queryBuilder);
        query.setFirstResult(pageQuery.getFirstResult());
        query.setMaxResults(pageQuery.getMaxResult());
        List<User> resultList = query.getResultList().stream()
                .map(UserEntity::toUser)
                .toList();
        log.info("Founded users: {}", resultList);
        entityManager.clear();
        return new PageQueryResult<>(resultList, getTotalElements());
    }

    private long getTotalElements() {
        log.info("Start count users");
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<UserEntity> root = query.from(UserEntity.class);
        query.select(builder.count(root));
        long result = entityManager.createQuery(query).getSingleResult();
        log.info("Users count result: {}", result);
        entityManager.clear();
        return result;
    }
}
