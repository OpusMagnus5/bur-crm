package pl.bodzioch.damian.dto;

import java.io.Serializable;
import java.util.List;

public record GetAllCustomersResponse(
        List<CustomerData> customers
) implements Serializable {
}
