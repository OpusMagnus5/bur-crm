package pl.bodzioch.damian.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
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

        Optional<UserEntityRead> user = session.byNaturalId(UserEntityRead.class).using("email", email).loadOptional();
        if (user.isPresent()) {
            log.info("User with the email: {} found: {}", email, user.get());
        } else {
            log.info("User with the email: {} NOT found", email);
        }
        session.clear();
        return user.map(UserEntityRead::toUser);
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public PageQueryResult<User> getUsers(PageQuery pageQuery) {
        log.info("Start search all users");
        CriteriaQuery<UserEntityRead> queryBuilder = entityManager.getCriteriaBuilder().createQuery(UserEntityRead.class);
        queryBuilder.from(UserEntityRead.class);
        TypedQuery<UserEntityRead> query = entityManager.createQuery(queryBuilder);
        query.setFirstResult(pageQuery.getFirstResult());
        query.setMaxResults(pageQuery.getMaxResult());
        List<UserEntityRead> result = query.getResultList();
        log.info("Founded users: {}", result);
        entityManager.clear();

        List<User> users = result.stream()
                .map(UserEntityRead::toUser)
                .toList();
        return new PageQueryResult<>(users, getTotalElements());
    }

    @Override
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Optional<User> getById(Long id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserEntityRead> query = builder.createQuery(UserEntityRead.class);

        Root<UserEntityRead> root = query.from(UserEntityRead.class);
        root.join("creator", JoinType.LEFT);
        root.join("modifier", JoinType.LEFT);

        query.select(root).where(builder.equal(root.get("id"), id));

        try {
            UserEntityRead result = entityManager.createQuery(query).getSingleResult();
            log.info("User with the id: {} found: {}", id, result);
            entityManager.clear();
            return Optional.of(result.toUser());
        } catch (NoResultException e) {
            log.info("User with the id: {} NOT found", id);
        }

        entityManager.clear();
        return Optional.empty();
    }

    private long getTotalElements() {
        log.info("Start count users");
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<UserEntityRead> root = query.from(UserEntityRead.class);
        query.select(builder.count(root));
        long result = entityManager.createQuery(query).getSingleResult();
        log.info("Users count result: {}", result);
        entityManager.clear();
        return result;
    }
}
