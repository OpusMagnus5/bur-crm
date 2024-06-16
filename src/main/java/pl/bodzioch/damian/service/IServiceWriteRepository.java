package pl.bodzioch.damian.service;

import jakarta.transaction.Transactional;

import java.util.List;

interface IServiceWriteRepository {
	@Transactional(Transactional.TxType.REQUIRED)
	void createOrUpdate(Service service);

    @Transactional(Transactional.TxType.REQUIRED)
    void updateStatus(List<Service> service);

    @Transactional(Transactional.TxType.REQUIRED)
    void delete(Long id);
}
