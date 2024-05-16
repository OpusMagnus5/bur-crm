package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record CreateNewCustomerResponse(
        String message
) implements Serializable {
}
