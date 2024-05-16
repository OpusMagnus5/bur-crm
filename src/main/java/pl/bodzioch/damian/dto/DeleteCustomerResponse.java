package pl.bodzioch.damian.dto;

import java.io.Serializable;

public record DeleteCustomerResponse(
        String message
) implements Serializable {
}
