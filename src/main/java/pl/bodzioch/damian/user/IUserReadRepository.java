package pl.bodzioch.damian.user;

import jakarta.transaction.Transactional;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import java.util.Optional;

interface IUserReadRepository {

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Optional<User> getByEmail(String email);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    PageQueryResult<User> getUsers(PageQuery pageQuery);

	@Transactional(Transactional.TxType.NOT_SUPPORTED)
	Optional<User> getById(Long id);
}
