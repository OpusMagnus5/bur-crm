package pl.bodzioch.damian.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.customer.query_dto.GetCustomerDetailsQuery;
import pl.bodzioch.damian.customer.query_dto.GetCustomerDetailsQueryResult;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.infrastructure.query.QueryHandler;
import pl.bodzioch.damian.value_object.ErrorData;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetCustomerDetailsQueryHandler implements QueryHandler<GetCustomerDetailsQuery, GetCustomerDetailsQueryResult> {

    private final ICustomerReadRepository readRepository;

    @Override
    public Class<GetCustomerDetailsQuery> queryClass() {
        return GetCustomerDetailsQuery.class;
    }

    @Override
    public GetCustomerDetailsQueryResult handle(GetCustomerDetailsQuery command) {
        Customer customer = readRepository.getDetails(command.id()).orElseThrow(() -> buildProgramByIdNotFound(command.id()));
        return new GetCustomerDetailsQueryResult(new CustomerDto(customer));
    }

    private AppException buildProgramByIdNotFound(Long id) {
        return new AppException(
                "Not found customer with id: " + id,
                HttpStatus.NOT_FOUND,
                List.of(new ErrorData("error.client.customerByIdNotFound", List.of(id.toString())))
        );
    }
}
