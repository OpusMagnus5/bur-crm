package pl.bodzioch.damian.service;

import jakarta.transaction.Transactional;

interface IServiceWriteRepository {
	@Transactional(Transactional.TxType.REQUIRED)
	void createNew(Service service);
}
