package pl.bodzioch.damian.coach;

import jakarta.transaction.Transactional;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import java.util.Optional;

interface ICoachReadRepository {
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Optional<Coach> getByNip(String pesel);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    PageQueryResult<Coach> getPage(PageQuery pageQuery);

	@Transactional(Transactional.TxType.NOT_SUPPORTED)
	Optional<Coach> getDetails(Long id);
}
