package pl.bodzioch.damian.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.customer.query_dto.GetAllCustomersQuery;
import pl.bodzioch.damian.customer.query_dto.GetAllCustomersQueryResult;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetAllCustomersQueryHandler implements QueryHandler<GetAllCustomersQuery, GetAllCustomersQueryResult> {

    private final ICustomerReadRepository readRepository;

    @Override
    public Class<GetAllCustomersQuery> queryClass() {
        return GetAllCustomersQuery.class;
    }

    @Override
    @Cacheable("customers")
    public GetAllCustomersQueryResult handle(GetAllCustomersQuery query) {
        List<CustomerDto> customers = readRepository.getAll().stream()
                .map(CustomerDto::new)
                .toList();
        return new GetAllCustomersQueryResult(customers);
    }
}
