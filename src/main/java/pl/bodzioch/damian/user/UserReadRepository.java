package pl.bodzioch.damian.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
class UserReadRepository implements IUserReadRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<User> getByEmail(String email) {
        log.info("Search user by email: {}", email);
        Session session = entityManager.unwrap(Session.class);
        Optional<UserEntity> user = session.byNaturalId(UserEntity.class).using("email", email).loadOptional();
        session.clear();
        if (user.isPresent()) {
            log.info("User with the email: {} found: {}", email, user.get());
        } else {
            log.info("User with the email: {} NOT found", email);
        }
        return user.map(UserEntity::toUser);
    }
}
