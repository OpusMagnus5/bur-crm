package pl.bodzioch.damian.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.customer.query_dto.GetCustomerByNipQuery;
import pl.bodzioch.damian.customer.query_dto.GetCustomerByNipQueryResult;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Component
@RequiredArgsConstructor
class GetCustomerByNipQueryHandler implements QueryHandler<GetCustomerByNipQuery, GetCustomerByNipQueryResult> {

    private final ICustomerReadRepository readRepository;

    @Override
    public Class<GetCustomerByNipQuery> queryClass() {
        return GetCustomerByNipQuery.class;
    }

    @Override
    public GetCustomerByNipQueryResult handle(GetCustomerByNipQuery query) {
        Customer customer = readRepository.getByNip(query.nip()).orElseThrow(() -> buildCustomerByNipNotFound(query.nip()));
        return new GetCustomerByNipQueryResult(new CustomerDto(customer));
    }

    private AppException buildCustomerByNipNotFound(Long nip) {
        return new AppException(
                "Service Provider with nip: " + nip + " not found",
                HttpStatus.NOT_FOUND,
                List.of(new ErrorData("error.client.customerByNipNotFound", List.of(nip.toString())))
        );
    }
}
