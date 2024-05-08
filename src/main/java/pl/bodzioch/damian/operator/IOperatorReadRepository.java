package pl.bodzioch.damian.operator;

import jakarta.transaction.Transactional;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import java.util.Optional;

interface IOperatorReadRepository {
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    Optional<Operator> getByName(String name);

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    PageQueryResult<Operator> getPage(PageQuery pageQuery);

	@Transactional(Transactional.TxType.NOT_SUPPORTED)
	Optional<Operator> getDetails(Long id);
}
