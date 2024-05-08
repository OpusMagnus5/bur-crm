package pl.bodzioch.damian.operator;

import jakarta.transaction.Transactional;

interface IOperatorWriteRepository {
    @Transactional(Transactional.TxType.REQUIRED)
    void createNew(Operator operator);

    @Transactional(Transactional.TxType.REQUIRED)
    void delete(Long id);

	@Transactional(Transactional.TxType.REQUIRED)
	void update(Operator operator);
}
