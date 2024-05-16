package pl.bodzioch.damian.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.customer.query_dto.GetCustomerPageQuery;
import pl.bodzioch.damian.customer.query_dto.GetCustomerPageQueryResult;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.value_object.PageQuery;
import pl.bodzioch.damian.value_object.PageQueryResult;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetCustomerPageQueryHandler implements QueryHandler<GetCustomerPageQuery, GetCustomerPageQueryResult> {

    private final ICustomerReadRepository readRepository;


    @Override
    public Class<GetCustomerPageQuery> queryClass() {
        return GetCustomerPageQuery.class;
    }

    @Override
    public GetCustomerPageQueryResult handle(GetCustomerPageQuery query) {
        PageQuery pageQuery = new PageQuery(query.pageNumber(), query.pageSize(), query.filters());
        PageQueryResult<Customer> result = readRepository.getPage(pageQuery);
        List<CustomerDto> customers = result.elements().stream()
                .map(CustomerDto::new)
                .toList();
        return new GetCustomerPageQueryResult(customers, result.totalElements());
    }
}
