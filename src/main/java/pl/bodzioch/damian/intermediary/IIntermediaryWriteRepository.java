package pl.bodzioch.damian.intermediary;

import jakarta.transaction.Transactional;

interface IIntermediaryWriteRepository {
    @Transactional(Transactional.TxType.REQUIRED)
    void createNew(Intermediary serviceProvider);

    @Transactional(Transactional.TxType.REQUIRED)
    void delete(Long id);

	@Transactional(Transactional.TxType.REQUIRED)
	void update(Intermediary operator);
}
