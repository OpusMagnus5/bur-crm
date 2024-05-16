package pl.bodzioch.damian.customer.query_dto;

import pl.bodzioch.damian.customer.CustomerDto;
import pl.bodzioch.damian.utils.CipherComponent;

import java.io.Serializable;

public record CustomerData(
        String id,
        String name,
        String nip
) implements Serializable {

    public CustomerData(CustomerDto customer, CipherComponent cipher) {
        this(
                cipher.encryptMessage(customer.id().toString()),
                customer.name(),
                customer.nip().toString()
        );
    }
}
