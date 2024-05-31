package pl.bodzioch.damian.service;

import jakarta.transaction.Transactional;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

interface IServiceReadRepository {
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    PageQueryResult<Service> getPage(PageQuery pageQuery);
}
