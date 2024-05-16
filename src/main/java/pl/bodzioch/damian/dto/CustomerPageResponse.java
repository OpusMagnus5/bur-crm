package pl.bodzioch.damian.dto;

import pl.bodzioch.damian.customer.query_dto.CustomerData;

import java.io.Serializable;
import java.util.List;

public record CustomerPageResponse(
        List<CustomerData> customers,
        Long totalCustomers
) implements Serializable {
}
