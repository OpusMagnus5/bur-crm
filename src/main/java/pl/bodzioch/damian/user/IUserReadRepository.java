package pl.bodzioch.damian.user;

import jakarta.transaction.Transactional;
import pl.bodzioch.damian.valueobject.PageQuery;
import pl.bodzioch.damian.valueobject.PageQueryResult;

import java.util.Optional;

interface IUserReadRepository {

    Optional<User> getByEmail(String email);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    PageQueryResult<User> getUsers(PageQuery pageQuery);
}
